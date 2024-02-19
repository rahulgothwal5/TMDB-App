package com.warlock.tmdb.ui.fragments.withDb

import androidx.lifecycle.*
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.warlock.tmdb.data.db.dao.MovieListDao
import com.warlock.tmdb.data.network.TMDBApi
import com.warlock.tmdb.data.network.TMDBApi.Companion.TMDB_API_KEY
import com.warlock.tmdb.ui.fragments.withDb.withPaging.MovieBoundaryCallBack
import com.warlock.tmdb.data.db.entity.MovieResult
import com.warlock.tmdb.data.db.entity.PopularListingData
import com.warlock.tmdb.data.db.entity.SearchResponseData
import com.warlock.tmdb.util.AppLog
import com.warlock.tmdb.util.network.Listing
import com.warlock.tmdb.util.network.NetworkBoundResource
import com.warlock.tmdb.util.network.NetworkState
import com.warlock.tmdb.util.network.Resource
import com.warlock.tmdb.util.paging.BaseListingResponse
import com.warlock.tmdb.util.paging.DataSourceFactory
import retrofit2.Call


class DbRepo(private val TMDBApi: TMDBApi, private val dao: MovieListDao) {

    fun serachMovies(q: String): LiveData<Resource<List<MovieResult>>> {
        return object : NetworkBoundResource<List<MovieResult>, SearchResponseData>() {
            override suspend fun saveCallResult(item: SearchResponseData) {
                item.movieResults.map {
                    dao.insert(it)
                }
            }

            override fun shouldFetch(data: List<MovieResult>?): Boolean {
                return data.isNullOrEmpty()
            }

            override suspend fun loadFromDb(): LiveData<List<MovieResult>> {
                return dao.getMovies(q)
            }

            override fun createCall(): LiveData<Resource<SearchResponseData>> {
                AppLog.d(q)
                return TMDBApi.searchMovies(q, TMDB_API_KEY)
            }
        }.asLiveDataBlock()
    }

    fun getPopular(page: Int): LiveData<Resource<List<MovieResult>>> {
        return object : NetworkBoundResource<List<MovieResult>, PopularListingData>() {
            override suspend fun saveCallResult(item: PopularListingData) {
                item.movieResults.map {
                    dao.insert(it)
                }
            }

            override fun shouldFetch(data: List<MovieResult>?): Boolean {
                return data.isNullOrEmpty()
            }

            override suspend fun loadFromDb(): LiveData<List<MovieResult>> {
                return dao.getMovies()
            }

            override fun createCall(): LiveData<Resource<PopularListingData>> {
                return TMDBApi.getPopularMovies(TMDB_API_KEY, page)
            }
        }.asLiveDataBlock()
    }

    fun getMoviePaged(): Listing<MovieResult> {
        val source = object : DataSourceFactory<MovieResult>() {
            override fun makeCall(
                page: Int,
                pageSize: Int
            ): Call<BaseListingResponse<MovieResult>> {
                return TMDBApi.getPopularMoviesPagedDataSource(api_key = TMDB_API_KEY, page = page)
            }
        }

        val refreshState = source.sourceLiveData.switchMap {
            it.initialLoad
        }

        val pagedList: LiveData<PagedList<MovieResult>> = source.toLiveData(5)
        return Listing(
            pagedList = pagedList,
            networkState = source.sourceLiveData.switchMap {
                it.networkState
            },
            retry = {
                source.sourceLiveData.value?.retryAllFailed()
            },
            refresh = {
                source.sourceLiveData.value?.invalidate()
            },
            refreshState = refreshState
        )
    }

    fun getListingFromDataBase(): Listing<MovieResult> {
        val boundaryCallback = MovieBoundaryCallBack(
            movieDao = dao,
            webservice = TMDBApi
        )

        val pagedList = dao.getMoviesDataSource().toLiveData(
            config,
            boundaryCallback = boundaryCallback
        )

        val refreshTrigger = MutableLiveData<Unit>()
        val refreshState = refreshTrigger.switchMap {
            refresh()
        }


        return Listing(
            pagedList = pagedList,
            networkState = boundaryCallback.networkState,
            retry = {
                retry()
            },
            refresh = {
                refreshTrigger.postValue(Unit)
            },
            refreshState = refreshState
        )
    }

    private fun retry() = Unit


    private fun refresh(): LiveData<NetworkState> {
//        val networkState = MutableLiveData<NetworkState>()
//        networkState.postValue(NetworkState.LOADING)
//        ApiRepository.callApi(apiService.discoverMovieAsync(1),
//            object : ApiCallback<DiscoverMoviesResponse> {
//                override fun onException(error: Throwable) {
//                    networkState.postValue(NetworkState.error(error.message))
//                }
//
//                override fun onError(errorModel: ApiError) {
//                    networkState.postValue(NetworkState.error(errorModel.statusMessage))
//                }
//
//                override fun onSuccess(t: DiscoverMoviesResponse?) {
//                    networkState.postValue(NetworkState.LOADED)
//                    t?.movies?.let {
//                        movieDao.nukeTable()
//                        insertIntoDatabase(t.movies)
//                    }
//                }
//            })

        return liveData { emit(NetworkState.LOADED) }
    }

    companion object {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setPageSize(20)
            .build()
    }
}
