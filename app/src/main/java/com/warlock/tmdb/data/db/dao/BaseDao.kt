package com.warlock.tmdb.data.db.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface BaseDao<T> {
    /**
     * use to insert T into database
     * @param obj of T
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(obj: T)

    /**
     * use to insert list of T into database
     * @param obj of T
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(obj: List<T>)

    /**
     * use to update T
     * @param obj of T
     */
    @Update
    suspend fun update(obj: T)

    /**
     * use to delete T from database
     * @param obj of T
     */
    @Delete
    suspend fun delete(obj: T)

    /**
     * use to delete list of T from database
     * @param obj of T
     */
    @Delete
    suspend fun delete(obj: List<T>)
}