package com.warlock.tmdb.ui.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.warlock.tmdb.data.db.entity.*
import com.warlock.tmdb.data.network.TMDBApi
import com.warlock.tmdb.data.network.TMDBApi.Companion.TMDB_API_KEY
import com.warlock.tmdb.util.AppLog
import com.warlock.tmdb.util.network.Listing
import com.warlock.tmdb.util.network.NetworkBoundResource
import com.warlock.tmdb.util.network.NetworkBoundResourceWithOutCache
import com.warlock.tmdb.util.network.Resource
import com.warlock.tmdb.util.paging.BaseListingResponse
import com.warlock.tmdb.util.paging.DataSourceFactory
import retrofit2.Call


class PopularListingRepo(private val tmdbApi: TMDBApi) {

    fun searchMovies(q: String): LiveData<Resource<SearchResponseData?>> {
        return object : NetworkBoundResourceWithOutCache<SearchResponseData>() {
            override fun createCall(): LiveData<Resource<SearchResponseData>> {
                AppLog.d(q)
                return tmdbApi.searchMovies(q, TMDB_API_KEY)
            }
        }.asLiveDataBlock()
    }



    fun getPopular(page: Int): LiveData<Resource<PopularListingData?>> {
        return object : NetworkBoundResourceWithOutCache<PopularListingData>() {
            override fun createCall(): LiveData<Resource<PopularListingData>> {
                return tmdbApi.getPopularMovies(TMDB_API_KEY, page)
            }
        }.asLiveDataBlock()
    }


    fun getMoviePaged(): Listing<MovieResult> {
        val source = object : DataSourceFactory<MovieResult>() {
            override fun makeCall(
                page: Int,
                pageSize: Int
            ): Call<BaseListingResponse<MovieResult>> {
                return tmdbApi.getPopularMoviesPagedDataSource(api_key = TMDB_API_KEY, page = page)
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