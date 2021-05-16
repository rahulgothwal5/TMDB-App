package com.warlock.tmdb.data.db.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import com.warlock.tmdb.data.db.entity.MovieResult

@Dao
interface MovieListDao : BaseDao<MovieResult> {
    @Query("SELECT * FROM MovieResult WHERE title LIKE:query")
    fun getMovies(query: String): LiveData<List<MovieResult>>

    @Query("SELECT * FROM MovieResult")
    fun getMovies(): LiveData<List<MovieResult>>

    @Query("SELECT COUNT(id) FROM MovieResult")
    fun getCount(): Int

    @Query("SELECT * FROM MovieResult")
    fun getMoviesDataSource(): DataSource.Factory<Int, MovieResult>
}