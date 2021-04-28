package com.example.themovie.model

class MovieCardSourceImpl : MovieCardSource {
    override fun getDataFromLocalSource(): List<GenresData> = GetData().getGenresData()
}