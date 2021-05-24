package com.example.themovie.model.database.notes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NotesEntity(
    @PrimaryKey(autoGenerate = false)
    val movieId: Int?,
    val note: String?,
)