@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.warlock.tmdb.data.db.entity

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList


data class PopularListingData(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val movieResults: ArrayList<MovieResult>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)

@Entity
data class MovieResult (
    @SerializedName("adult")
    val adult: Boolean,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @PrimaryKey
    @SerializedName("id")
    val id: Int,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("original_title")
    val originalTitle: String,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("popularity")
    val popularity: Double,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("release_date")
    val releaseDate: String?,
    @SerializedName("title")
    val title: String,
    @SerializedName("video")
    val video: Boolean,
    @SerializedName("vote_average")
    val voteAverage: String,
    @SerializedName("vote_count")
    val voteCount: Int,
    @SerializedName("genre_ids")
    val genreIds: List<Int>?
)

data class SearchResponseData(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val movieResults: ArrayList<MovieResult>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)

class IDConverter {
    @TypeConverter
    fun storedStringToMyObjects(data: String?): List<Int>? {
        if (data.isNullOrEmpty()) {
            return Collections.emptyList()
        }
        val listType: Type = object : TypeToken<List<Int>?>() {}.type
        return Gson().fromJson<List<Int>>(data, listType)
    }

    @TypeConverter
    fun myObjectsToStoredString(myObjects: List<Int>?): String? {
        return Gson().toJson(myObjects)
    }
}


