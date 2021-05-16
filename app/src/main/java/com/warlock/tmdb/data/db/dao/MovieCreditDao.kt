package com.warlock.tmdb.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.warlock.tmdb.data.db.entity.MovieCreditsData

@Dao
interface MovieCreditDao : BaseDao<MovieCreditsData> {
    @Query("SELECT * FROM MovieCreditsData WHERE id LIKE:query")
    fun getCredits(query: Int): LiveData<MovieCreditsData>
}