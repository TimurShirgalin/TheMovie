package com.example.themovie.model

data class GenresData(
    val genres: String,
    val movieData: List<MovieData>
)

data class MovieData(
    val movieName: String,
    val genre: String,
    val description: String,
    val image: Int
)
