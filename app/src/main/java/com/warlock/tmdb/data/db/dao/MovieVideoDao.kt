package com.warlock.tmdb.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.warlock.tmdb.data.db.entity.MovieVideosDataResponse

@Dao
interface MovieVideoDao : BaseDao<MovieVideosDataResponse> {
    @Query("SELECT * FROM MovieVideosDataResponse WHERE id LIKE:query")
    fun getVideos(query: Int): LiveData<MovieVideosDataResponse>
}