package com.example.themovie.model.database.notes

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NotesDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addNote(entity: NotesEntity)

    @Query("DELETE FROM NotesEntity WHERE movieId LIKE :movieId")
    fun deleteNote(movieId: Int?)

    @Query("SELECT * FROM NotesEntity WHERE movieId LIKE :movieId")
    fun getNote(movieId: Int?): NotesEntity?
}