package com.warlock.tmdb.ui.fragments.withDb.withPaging

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.warlock.tmdb.data.db.dao.MovieListDao
import com.warlock.tmdb.data.network.TMDBApi
import com.warlock.tmdb.data.db.entity.MovieResult
import com.warlock.tmdb.util.network.NetworkState
import com.warlock.tmdb.util.network.Status
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import retrofit2.await
import java.io.IOException

class MovieBoundaryCallBack(
    private val movieDao: MovieListDao,
    private val webservice: TMDBApi
) : PagedList.BoundaryCallback<MovieResult>() {
    val networkState = MutableLiveData<NetworkState>()
    val initialLoad = MutableLiveData<NetworkState>()
    private var retry: (() -> Any)? = null

    override fun onZeroItemsLoaded() {
        callMovieWebService(
            1
        )
    }

    private fun getPageNumber() = runBlocking(IO) {
        val movieCount = movieDao.getCount()
        (movieCount / 20) + 1
    }

    override fun onItemAtEndLoaded(itemAtEnd: MovieResult) {
        callMovieWebService(getPageNumber())
    }

    override fun onItemAtFrontLoaded(itemAtFront: MovieResult) {
        //EMPTY
    }

    private fun callMovieWebService(i: Int) {
        if (networkState.value?.status == Status.RUNNING)
            return
        networkState.postValue(NetworkState.LOADING)
        val request = webservice.getPopularMoviesPaged(
            api_key = TMDBApi.TMDB_API_KEY, page = i
        )
        CoroutineScope(IO).launch {
            try {
                val response = request.await()
                response.movieResults
                retry = null
                networkState.postValue(NetworkState.LOADED)
                initialLoad.postValue(NetworkState.LOADED)
                GlobalScope.launch(Dispatchers.IO) {
                    movieDao.insert(response.movieResults)
                }
            } catch (ioException: IOException) {
                networkState.postValue(NetworkState.error(ioException.message))
                val error = NetworkState.error(ioException.message ?: "unknown error")
                networkState.postValue(error)
                initialLoad.postValue(error)
            }
        }
    }
}