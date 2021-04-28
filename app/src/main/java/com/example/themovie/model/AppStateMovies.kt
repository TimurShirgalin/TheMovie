package com.example.themovie.model

sealed class AppStateMovies {
    data class Success(val data: List<GenresData>) : AppStateMovies()
    data class Error(val error: Throwable) : AppStateMovies()
    object Loading : AppStateMovies()
}