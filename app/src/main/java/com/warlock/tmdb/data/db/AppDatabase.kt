package com.warlock.tmdb.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.warlock.tmdb.data.db.dao.*
import com.warlock.tmdb.data.db.entity.*


@Database(
    entities = [MovieResult::class,
        GenreX::class,
        MovieCreditsData::class,
        MovieVideosDataResponse::class,
        MovieDetailDataResponse::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    IDConverter::class,
    MovieGenreConverter::class,
    MovieVideoConverter::class,
    MovieCreditCrewConverter::class,
    BelongsToCollectionConverter::class,
    MovieCreditCastConverter::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieListDao
    abstract fun genreDao(): GenreListDao
    abstract fun movieCreditsDao(): MovieCreditDao
    abstract fun movieDetailDao(): MovieDetailDao
    abstract fun movieVideosDao(): MovieVideoDao

    companion object {

        private const val DATABASE_NAME = "tmdb.db"

        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        /**
         * methos used to get instance of AppDatabase
         * @param context to build database instance
         * @return AppDatabase
         */
        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        /**
         * Create and pre-populate the database.
         * @param context to build database instance
         * @return AppDatabase
         */
        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .addCallback(object : RoomDatabase.Callback() {
                })
                .build()
        }
    }
}