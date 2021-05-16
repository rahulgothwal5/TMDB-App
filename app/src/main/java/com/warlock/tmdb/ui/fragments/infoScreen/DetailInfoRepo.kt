package com.warlock.tmdb.ui.fragments.infoScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.warlock.tmdb.data.db.AppDatabase
import com.warlock.tmdb.data.db.entity.*
import com.warlock.tmdb.data.network.TMDBApi
import com.warlock.tmdb.data.network.TMDBApi.Companion.TMDB_API_KEY
import com.warlock.tmdb.util.network.Listing
import com.warlock.tmdb.util.network.NetworkBoundResource
import com.warlock.tmdb.util.network.NetworkBoundResourceWithOutCache
import com.warlock.tmdb.util.network.Resource
import com.warlock.tmdb.util.paging.BaseListingResponse
import com.warlock.tmdb.util.paging.DataSourceFactory
import retrofit2.Call


class DetailInfoRepo(private val tmdbApi: TMDBApi, private val appDatabase: AppDatabase) {

    fun getMovieCreditList(movie_id: Int): LiveData<Resource<MovieCreditsData>> {
        return object : NetworkBoundResource<MovieCreditsData, MovieCreditsData>() {
            override suspend fun saveCallResult(item: MovieCreditsData) {
                appDatabase.movieCreditsDao().insert(item)
            }

            override fun shouldFetch(data: MovieCreditsData?): Boolean {
                return data == null
            }

            override suspend fun loadFromDb(): LiveData<MovieCreditsData> {
                return appDatabase.movieCreditsDao().getCredits(movie_id)
            }

            override fun createCall(): LiveData<Resource<MovieCreditsData>> {
                return tmdbApi.getMovieCreditsList(movie_id, TMDB_API_KEY)
            }
        }.asLiveDataBlock()
    }

    fun getMovieVideoList(movie_id: Int): LiveData<Resource<MovieVideosDataResponse>> {
        return object : NetworkBoundResource<MovieVideosDataResponse, MovieVideosDataResponse>() {
            override suspend fun saveCallResult(item: MovieVideosDataResponse) {
                appDatabase.movieVideosDao().insert(item)
            }

            override fun shouldFetch(data: MovieVideosDataResponse?): Boolean {
                return data == null
            }

            override suspend fun loadFromDb(): LiveData<MovieVideosDataResponse> {
                return appDatabase.movieVideosDao().getVideos(movie_id)
            }

            override fun createCall(): LiveData<Resource<MovieVideosDataResponse>> {
                return tmdbApi.getMovieVideosList(movie_id, TMDB_API_KEY)
            }
        }.asLiveDataBlock()
    }

    fun getMovieDetailsList(movie_id: Int): LiveData<Resource<MovieDetailDataResponse>> {
        return object : NetworkBoundResource<MovieDetailDataResponse, MovieDetailDataResponse>() {
            override suspend fun saveCallResult(item: MovieDetailDataResponse) {
                appDatabase.movieDetailDao().insert(item)
            }

            override fun shouldFetch(data: MovieDetailDataResponse?): Boolean {
                return data == null
            }

            override suspend fun loadFromDb(): LiveData<MovieDetailDataResponse> {
                return appDatabase.movieDetailDao().getMovieDetail(movie_id)
            }

            override fun createCall(): LiveData<Resource<MovieDetailDataResponse>> {
                return tmdbApi.getMovieDetail(movie_id, TMDB_API_KEY)
            }
        }.asLiveDataBlock()
    }

    fun getSimilar(movie_id: Int): LiveData<Resource<PopularListingData?>> {
        return object : NetworkBoundResourceWithOutCache<PopularListingData>() {
            override fun createCall(): LiveData<Resource<PopularListingData>> {
                return tmdbApi.getSimilarMovies(movie_id, TMDB_API_KEY)
            }
        }.asLiveDataBlock()
    }

    fun getMoviePaged(movie_id: Int): Listing<MovieResult> {
        val source = object : DataSourceFactory<MovieResult>() {
            override fun makeCall(
                page: Int,
                pageSize: Int
            ): Call<BaseListingResponse<MovieResult>> {
                return tmdbApi.getSimilarMovies(
                    movie_id = movie_id,
                    api_key = TMDB_API_KEY,
                    page = page
                )
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
}