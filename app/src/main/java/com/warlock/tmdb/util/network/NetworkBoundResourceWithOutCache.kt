package com.warlock.tmdb.util.network

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map

/**
 * A generic class that can provide a resource backed the network.
 *
 * You can read more about it in the [Architecture
 * Guide](https://developer.android.com/arch).
 *
 * @param <RequestType>*/
abstract class NetworkBoundResourceWithOutCache<RequestType> {

    /**
     * return the network response backed by resource
     * @return LiveData<Resource<RequestType?>>
     */
    @MainThread
    fun asLiveDataBlock(): LiveData<Resource<RequestType?>> = liveData {
        emit(Resource.loading())
        emitSource(createCall().map {
            if (it.status.isSuccessful()) {
                Resource.success(it.data)
            } else {
                onFetchFailed()
                Resource.error(it.error)
            }
        })
    }

    /**
     * called when network call failed
     */
    private fun onFetchFailed() {}

    /**
     * its make a network call
     */
    @MainThread
    protected abstract fun createCall(): LiveData<Resource<RequestType>>
}