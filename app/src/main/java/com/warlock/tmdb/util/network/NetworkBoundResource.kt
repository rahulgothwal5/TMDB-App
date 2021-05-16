package com.warlock.tmdb.util.network


import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


/**
 * A generic class that can provide a resource backed by both the sqlite database and the network.
 *
 * You can read more about it in the [Architecture
 * Guide](https://developer.android.com/arch).
</RequestType></ResultType> */
abstract class NetworkBoundResource<ResultType, RequestType> {


    fun asLiveDataBlock(): LiveData<Resource<ResultType>> = liveData(Dispatchers.IO) {
        // Send loading state to UI
        emit(Resource.loading())

        // load from db and check that should call network or not
        emitSource(loadFromDb().switchMap { dbResult ->
            if (shouldFetch(dbResult)) {
                createCall().switchMap { apiResponse ->
                    if (apiResponse.status.isSuccessful()) {
                        GlobalScope.launch {
                            processResponse(apiResponse)?.let { requestType ->
                                saveCallResult(
                                    requestType
                                )
                            }
                        }
                        liveData { emit(Resource.success(dbResult)) }
                    } else {
                        onFetchFailed()
                        liveData { emit(Resource.error(apiResponse.error)) }
                    }
                }
            } else {
                liveData { emit(Resource.success(dbResult)) }
            }
        })

    }


    /**
     * The final result LiveData
     */
    private val result = MediatorLiveData<Resource<ResultType?>>()

    init {
        // Send loading state to UI
        result.value = Resource.loading()
        val dbSource = runBlocking { loadFromDb() }
        result.addSource(dbSource) { data ->
            result.removeSource(dbSource)
            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource)
            } else {
                result.addSource(dbSource) { newData ->
                    setValue(
                        Resource.success(
                            newData
                        )
                    )
                }
            }
        }
    }


    /**
     * Fetch the data from network and persist into DB and then
     * send it back to UI.
     */
    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        val apiResponse = createCall()
        // we re-attach dbSource as a new source, it will dispatch its latest value quickly
        result.addSource(dbSource) { result.setValue(Resource.loading()) }
        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            result.removeSource(dbSource)

            response.apply {
                if (status.isSuccessful()) {
                    GlobalScope.launch(Dispatchers.IO) {
                        processResponse(this@apply)?.let { requestType -> saveCallResult(requestType) }
                        GlobalScope.launch(Dispatchers.Main) {
                            // we specially request a new live data,
                            // otherwise we will get immediately last cached value,
                            // which may not be updated with latest results received from network.
                            result.addSource(loadFromDb()) { newData ->
                                setValue(
                                    Resource.success(
                                        newData
                                    )
                                )
                            }
                        }
                    }
                } else {
                    onFetchFailed()
                    result.addSource(dbSource) {
                        result.setValue(
                            Resource.error(
                                error
                            )
                        )
                    }
                }
            }
        }
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType?>) {
        if (result.value != newValue) result.value = newValue
    }

    private fun onFetchFailed() {}

    fun asLiveData(): LiveData<Resource<ResultType?>> {
        return result
    }

    @WorkerThread
    private fun processResponse(response: Resource<RequestType>): RequestType? {
        return response.data
    }

    @WorkerThread
    protected abstract suspend fun saveCallResult(item: RequestType)

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean


    protected abstract suspend fun loadFromDb(): LiveData<ResultType>

    @MainThread
    protected abstract fun createCall(): LiveData<Resource<RequestType>>
}