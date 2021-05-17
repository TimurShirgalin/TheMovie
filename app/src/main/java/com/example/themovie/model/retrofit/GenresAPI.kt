package com.example.themovie.model.retrofit

import com.example.themovie.model.GenresData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GenresAPI {

    @GET("3/genre/movie/list")
    fun getGenres(
        @Query("api_key") api: String,
        @Query("language") language: String,
    ): Call<GenresData>
}