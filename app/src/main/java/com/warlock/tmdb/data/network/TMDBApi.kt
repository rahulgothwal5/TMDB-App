package com.warlock.tmdb.data.network

import androidx.lifecycle.LiveData
import com.warlock.tmdb.data.db.entity.*
import com.warlock.tmdb.util.network.LiveDataCallAdapterFactory
import com.warlock.tmdb.util.network.Resource
import com.warlock.tmdb.util.paging.BaseListingResponse
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBApi {

    @GET("3/movie/popular")
    fun getPopularMovies(
        @Query("api_key") api_key: String,
        @Query("page") page: Int
    ): LiveData<Resource<PopularListingData>>

    @GET("3/movie/popular")
    fun getPopularMoviesPaged(
        @Query("api_key") api_key: String,
        @Query("page") page: Int
    ): Call<PopularListingData>

    @GET("3/movie/popular")
    fun getPopularMoviesPagedDataSource(
        @Query("api_key") api_key: String,
        @Query("page") page: Int
    ): Call<BaseListingResponse<MovieResult>>

    @GET("/3/search/movie")
    fun searchMovies(
        @Query("query") query: String,
        @Query("api_key") api_key: String
    ): LiveData<Resource<SearchResponseData>>

    @GET("/3/genre/movie/list")
    fun getGenreList(
        @Query("api_key") api_key: String
    ): LiveData<Resource<Genre>>

    @GET("/3/movie/{movie_id}/credits")
    fun getMovieCreditsList(
        @Path("movie_id") movie_id:Int,
        @Query("api_key") api_key: String
    ): LiveData<Resource<MovieCreditsData>>

    @GET("/3/movie/{movie_id}/videos")
    fun getMovieVideosList(
        @Path("movie_id") movie_id:Int,
        @Query("api_key") api_key: String
    ): LiveData<Resource<MovieVideosDataResponse>>

    @GET("/3/movie/{movie_id}")
    fun getMovieDetail(
        @Path("movie_id") movie_id:Int,
        @Query("api_key") api_key: String
    ): LiveData<Resource<MovieDetailDataResponse>>

    @GET("/3/movie/{movie_id}/similar")
    fun getSimilarMovies(
        @Path("movie_id") movie_id:Int,
        @Query("api_key") api_key: String
    ): LiveData<Resource<PopularListingData>>

    @GET("/3/movie/{movie_id}/similar")
    fun getSimilarMovies(
        @Path("movie_id") movie_id:Int,
        @Query("api_key") api_key: String,
        @Query("page") page: Int
    ):  Call<BaseListingResponse<MovieResult>>

    companion object {
        private const val TMDB_URL = "https://api.themoviedb.org/"
        const val TMDB_API_KEY: String = "Your key here"
        const val POSTER_PATH = "https://image.tmdb.org/t/p/w300/"

        fun create(okHttpClient: OkHttpClient): TMDBApi = create(
            TMDB_URL.toHttpUrlOrNull()!!,
            okHttpClient
        )

        fun create(httpUrl: HttpUrl, okHttpClient: OkHttpClient): TMDBApi {
            return Retrofit.Builder()
                .baseUrl(httpUrl)
                .client(okHttpClient)
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TMDBApi::class.java)
        }



    }
}