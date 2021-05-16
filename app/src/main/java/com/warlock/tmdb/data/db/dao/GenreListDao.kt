package com.warlock.tmdb.data.db.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import com.warlock.tmdb.data.db.entity.GenreX
import com.warlock.tmdb.data.db.entity.MovieResult

@Dao
interface GenreListDao : BaseDao<GenreX> {
    @Query("SELECT * FROM GenreX")
    fun getAllGenre(): LiveData<List<GenreX>>
}