package com.example.themovie.model.database.favoriteMovies

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MoviesEntity(
    @PrimaryKey(autoGenerate = false)
    val movieId: Int?,
    val title: String?,
    val overview: String?,
    val poster_path: String?,
    val vote_average: Double?,
    val like: Boolean = false,
)