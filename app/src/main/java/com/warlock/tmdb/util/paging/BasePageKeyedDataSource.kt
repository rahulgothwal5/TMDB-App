package com.warlock.tmdb.util.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.warlock.tmdb.util.network.NetworkState
import retrofit2.Call
import java.io.IOException

/**
 *  * Incremental data loader for page-keyed content, where requests return keys for next/previous
 * pages.
 * <p>
 * Implement a DataSource using PageKeyedDataSource if you need to use data from page {@code N - 1}
 * to load page {@code N}. This is common, for example, in network APIs that include a next/previous
 * link or key with each page load.
 * <p>
 * @param <Key> Type of data used to query Value types out of the DataSource.
 * @param <Value> Type of items being loaded by the DataSource.
 */
abstract class BasePageKeyedDataSource<Value> : PageKeyedDataSource<Int, Value>() {

    // keep a function reference for the retry event
    private var retry: (() -> Any)? = null

    /**
     * There is no sync on the state because paging will always call loadInitial first then wait
     * for it to return some success value before calling loadAfter.
     */
    val networkState = MutableLiveData<NetworkState>()

    val initialLoad = MutableLiveData<NetworkState>()


    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.invoke()
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Value>
    ) {
        val request = makeCall(1, params.requestedLoadSize)
        networkState.postValue(NetworkState.LOADING)
        initialLoad.postValue(NetworkState.LOADING)

        try {
            val response = request.execute()
            if (response.isSuccessful) {
                val data = response.body()
                val items = data?.data ?: emptyList()
                val page = data?.page ?: 1
                retry = null
                networkState.postValue(NetworkState.LOADED)
                initialLoad.postValue(NetworkState.LOADED)
                callback.onResult(items, page, page.plus(1))
            } else {
                val errorBody = response.message()
                retry = {
                    loadInitial(params, callback)
                }
                val error = NetworkState.error(errorBody)
                networkState.postValue(error)
                initialLoad.postValue(error)
            }
        } catch (ioException: IOException) {
            retry = {
                loadInitial(params, callback)
            }
            val error = NetworkState.error(ioException.message ?: "unknown error")
            networkState.postValue(error)
            initialLoad.postValue(error)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Value>) {
        val request = makeCall(params.key, params.requestedLoadSize)
        networkState.postValue(NetworkState.LOADING)

        try {
            val response = request.execute()
            if (response.isSuccessful) {
                val data = response.body()
                val items = data?.data ?: emptyList()
                retry = null
                networkState.postValue(NetworkState.LOADED)
                callback.onResult(items, data!!.page.plus(1))
            } else {
                val errorBody = response.message()
                retry = {
                    loadAfter(params, callback)
                }
                val error = NetworkState.error(errorBody)
                networkState.postValue(error)
            }
        } catch (ioException: IOException) {
            retry = {
                loadAfter(params, callback)
            }
            val error = NetworkState.error(ioException.message ?: "unknown error")
            networkState.postValue(error)
        }

    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Value>) {
        // ignored, since we only ever append to our initial load
    }

    abstract fun makeCall(page: Int, pageSize: Int): Call<BaseListingResponse<Value>>
}