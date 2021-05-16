package com.warlock.tmdb.ui.fragments.homeFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.warlock.tmdb.data.db.AppDatabase
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


class HomeRepo(private val tmdbApi: TMDBApi, private val appDatabase: AppDatabase) {


    fun getGenreList(): LiveData<Resource<List<GenreX>>> {
        return object : NetworkBoundResource<List<GenreX>,Genre>() {
            override suspend fun saveCallResult(item: Genre) {
                item.genres?.map {
                    appDatabase.genreDao().insert(it)
                }
            }

            override fun shouldFetch(data: List<GenreX>?): Boolean {
                return data.isNullOrEmpty()
            }

            override suspend fun loadFromDb(): LiveData<List<GenreX>> {
                return appDatabase.genreDao().getAllGenre()
            }

            override fun createCall(): LiveData<Resource<Genre>> {
                return tmdbApi.getGenreList(TMDB_API_KEY)
            }

        }.asLiveDataBlock()
    }

}