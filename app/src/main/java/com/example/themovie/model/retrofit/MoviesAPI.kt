package com.example.themovie.model.retrofit

import com.example.themovie.model.MoviesData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesAPI {

    @GET("3/discover/movie")
    fun getMovies(
        @Query("api_key") api: String,
        @Query("language") language: String,
    ): Call<MoviesData>
}