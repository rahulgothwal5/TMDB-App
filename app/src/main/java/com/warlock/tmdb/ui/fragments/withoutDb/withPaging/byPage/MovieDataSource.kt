package com.warlock.tmdb.ui.fragments.withoutDb.withPaging.byPage

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.warlock.tmdb.data.network.TMDBApi
import com.warlock.tmdb.data.network.TMDBApi.Companion.TMDB_API_KEY
import com.warlock.tmdb.data.db.entity.MovieResult
import com.warlock.tmdb.data.db.entity.PopularListingData
import com.warlock.tmdb.util.network.NetworkState
import retrofit2.Call
import retrofit2.Response
import java.io.IOException


class MovieDataSource(val TMDBApi: TMDBApi, var position: Int) :
    PageKeyedDataSource<Int, MovieResult>() {


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
        callback: LoadInitialCallback<Int, MovieResult>
    ) {
        val request = TMDBApi.getPopularMoviesPaged(
            api_key = TMDB_API_KEY, page = position
        )
        networkState.postValue(NetworkState.LOADING)
        initialLoad.postValue(NetworkState.LOADING)

        // triggered by a refresh, we better execute sync
        try {
            val response = request.execute()
            val data = response.body()?.movieResults
            retry = null
            networkState.postValue(NetworkState.LOADED)
            initialLoad.postValue(NetworkState.LOADED)
            callback.onResult(data ?: arrayListOf(), null, position++)
        } catch (ioException: IOException) {
            retry = {
                loadInitial(params, callback)
            }
            val error = NetworkState.error(ioException.message ?: "unknown error")
            networkState.postValue(error)
            initialLoad.postValue(error)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, MovieResult>) {
        networkState.postValue(NetworkState.LOADING)
        TMDBApi.getPopularMoviesPaged(
            api_key = TMDB_API_KEY, page = position
        ).enqueue(
            object : retrofit2.Callback<PopularListingData> {
                override fun onFailure(call: Call<PopularListingData>, t: Throwable) {
                    retry = {
                        loadAfter(params, callback)
                    }
                    networkState.postValue(NetworkState.error(t.message ?: "unknown err"))
                }

                override fun onResponse(
                    call: Call<PopularListingData>,
                    response: Response<PopularListingData>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()?.movieResults
                        retry = null
                        callback.onResult(data ?: arrayListOf(), position++)
                        networkState.postValue(NetworkState.LOADED)
                    } else {
                        retry = {
                            loadAfter(params, callback)
                        }
                        networkState.postValue(
                            NetworkState.error("error code: ${response.code()}")
                        )
                    }
                }
            })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, MovieResult>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}