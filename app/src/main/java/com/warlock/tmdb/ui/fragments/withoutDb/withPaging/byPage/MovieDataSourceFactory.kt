package com.warlock.tmdb.ui.fragments.withoutDb.withPaging.byPage


import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.warlock.tmdb.data.network.TMDBApi
import com.warlock.tmdb.data.db.entity.MovieResult


class MovieDataSourceFactory(
    val TMDBApi: TMDBApi,
    val position: Int
) : DataSource.Factory<Int, MovieResult>() {
    val sourceLiveData =MutableLiveData<MovieDataSource>()
    override fun create(): DataSource<Int, MovieResult> {
        val source = MovieDataSource(TMDBApi, position)
        sourceLiveData.postValue(source)
        return source
    }


    //getter for itemlivedatasource
    fun getItemLiveDataSource(): MutableLiveData<MovieDataSource> {
        return sourceLiveData
    }
}