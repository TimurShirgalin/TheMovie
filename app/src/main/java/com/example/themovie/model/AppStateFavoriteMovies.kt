package com.example.themovie.model

sealed class AppStateFavoriteMovies {
    data class Success(val data: List<MoviesWithoutGenres>) : AppStateFavoriteMovies()
    data class Error(val error: Throwable) : AppStateFavoriteMovies()
    object Loading : AppStateFavoriteMovies()
}