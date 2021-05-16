package com.warlock.tmdb.data.db.entity
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*

@Entity
data class MovieCreditsData(
    @SerializedName("cast")
    val cast: List<Cast>?,
    @SerializedName("crew")
    val crew: List<Crew>?,
    @PrimaryKey
    @SerializedName("id")
    val id: Int?
)

data class Cast(
    @SerializedName("adult")
    val adult: Boolean?,
    @SerializedName("cast_id")
    val castId: Int?,
    @SerializedName("character")
    val character: String?,
    @SerializedName("credit_id")
    val creditId: String?,
    @SerializedName("gender")
    val gender: Int?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("known_for_department")
    val knownForDepartment: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("order")
    val order: Int?,
    @SerializedName("original_name")
    val originalName: String?,
    @SerializedName("popularity")
    val popularity: Double?,
    @SerializedName("profile_path")
    val profilePath: String?
)

data class Crew(
    @SerializedName("adult")
    val adult: Boolean?,
    @SerializedName("credit_id")
    val creditId: String?,
    @SerializedName("department")
    val department: String?,
    @SerializedName("gender")
    val gender: Int?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("job")
    val job: String?,
    @SerializedName("known_for_department")
    val knownForDepartment: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("original_name")
    val originalName: String?,
    @SerializedName("popularity")
    val popularity: Double?,
    @SerializedName("profile_path")
    val profilePath: String?
)

@Entity
data class MovieVideosDataResponse(
    @PrimaryKey
    @SerializedName("id")
    val id: Int?,
    @SerializedName("results")
    val results: List<RelatedVideos>?
)

data class RelatedVideos(
    @SerializedName("id")
    val id: String?,
    @SerializedName("iso_3166_1")
    val iso31661: String?,
    @SerializedName("iso_639_1")
    val iso6391: String?,
    @SerializedName("key")
    val key: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("site")
    val site: String?,
    @SerializedName("size")
    val size: Int?,
    @SerializedName("type")
    val type: String?
)

@Entity
data class MovieDetailDataResponse(
    @SerializedName("adult")
    val adult: Boolean?,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("belongs_to_collection")
    val belongsToCollection: BelongsToCollection?,
    @SerializedName("budget")
    val budget: Int?,
    @SerializedName("genres")
    val genres: List<GenreX>?,
    @SerializedName("homepage")
    val homepage: String?,
    @PrimaryKey
    @SerializedName("id")
    val id: Int?,
    @SerializedName("imdb_id")
    val imdbId: String?,
    @SerializedName("original_language")
    val originalLanguage: String?,
    @SerializedName("original_title")
    val originalTitle: String?,
    @SerializedName("overview")
    val overview: String?,
    @SerializedName("popularity")
    val popularity: Double?,
    @SerializedName("poster_path")
    val posterPath: String?,
//    @SerializedName("production_companies")
//    val productionCompanies: List<ProductionCompany>?,
//    @SerializedName("production_countries")
//    val productionCountries: List<ProductionCountry>?,
    @SerializedName("release_date")
    val releaseDate: String?,
    @SerializedName("revenue")
    val revenue: Int?,
    @SerializedName("runtime")
    val runtime: Int?,
//    @SerializedName("spoken_languages")
//    val spokenLanguages: List<SpokenLanguage>?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("tagline")
    val tagline: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("video")
    val video: Boolean?,
    @SerializedName("vote_average")
    val voteAverage: String?,
    @SerializedName("vote_count")
    val voteCount: Int?
)

data class BelongsToCollection(
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("poster_path")
    val posterPath: String?
)

data class GenreY(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?
)

data class ProductionCompany(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("logo_path")
    val logoPath: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("origin_country")
    val originCountry: String?
)

data class ProductionCountry(
    @SerializedName("iso_3166_1")
    val iso31661: String?,
    @SerializedName("name")
    val name: String?
)

data class SpokenLanguage(
    @SerializedName("english_name")
    val englishName: String?,
    @SerializedName("iso_639_1")
    val iso6391: String?,
    @SerializedName("name")
    val name: String?
)


class MovieCreditCastConverter {
    @TypeConverter
    fun storedStringToMyObjects(data: String?): List<Cast>? {
        if (data.isNullOrEmpty()) {
            return Collections.emptyList()
        }
        val listType: Type = object : TypeToken<List<Cast>?>() {}.type
        return Gson().fromJson<List<Cast>>(data, listType)
    }

    @TypeConverter
    fun myObjectsToStoredString(myObjects: List<Cast>?): String? {
        return Gson().toJson(myObjects)
    }
}

class MovieCreditCrewConverter {
    @TypeConverter
    fun storedStringToMyObjects(data: String?): List<Crew>? {
        if (data.isNullOrEmpty()) {
            return Collections.emptyList()
        }
        val listType: Type = object : TypeToken<List<Crew>?>() {}.type
        return Gson().fromJson<List<Crew>>(data, listType)
    }

    @TypeConverter
    fun myObjectsToStoredString(myObjects: List<Crew>?): String? {
        return Gson().toJson(myObjects)
    }
}

class MovieVideoConverter {
    @TypeConverter
    fun storedStringToMyObjects(data: String?): List<RelatedVideos>? {
        if (data.isNullOrEmpty()) {
            return Collections.emptyList()
        }
        val listType: Type = object : TypeToken<List<RelatedVideos>?>() {}.type
        return Gson().fromJson<List<RelatedVideos>>(data, listType)
    }

    @TypeConverter
    fun myObjectsToStoredString(myObjects: List<RelatedVideos>?): String? {
        return Gson().toJson(myObjects)
    }
}

class MovieGenreConverter {
    @TypeConverter
    fun storedStringToMyObjects(data: String?): List<GenreX>? {
        if (data.isNullOrEmpty()) {
            return Collections.emptyList()
        }
        val listType: Type = object : TypeToken<List<GenreX>?>() {}.type
        return Gson().fromJson<List<GenreX>>(data, listType)
    }

    @TypeConverter
    fun myObjectsToStoredString(myObjects: List<GenreX>?): String? {
        return Gson().toJson(myObjects)
    }
}

class BelongsToCollectionConverter {
    @TypeConverter
    fun storedStringToMyObjects(data: String?): BelongsToCollection? {
        if (data.isNullOrEmpty()) {
            return null
        }
        val listType: Type = object : TypeToken<BelongsToCollection?>() {}.type
        return Gson().fromJson<BelongsToCollection>(data, listType)
    }

    @TypeConverter
    fun myObjectsToStoredString(myObjects: BelongsToCollection?): String? {
        return Gson().toJson(myObjects)
    }
}

