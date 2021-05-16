package com.warlock.tmdb.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.warlock.tmdb.data.db.entity.MovieDetailDataResponse

@Dao
interface MovieDetailDao : BaseDao<MovieDetailDataResponse> {
    @Query("SELECT * FROM MovieDetailDataResponse WHERE id LIKE:query")
    fun getMovieDetail(query: Int): LiveData<MovieDetailDataResponse>
}