package com.example.themovie.model.database.notes

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.themovie.util.App

@Database(entities = [NotesEntity::class], version = 1, exportSchema = false)
abstract class NotesDatabase : RoomDatabase() {
    abstract fun notesDAO(): NotesDAO

    companion object {
        private const val DB_NAME = "note_database.db"
        val database: NotesDatabase by lazy {
            Room.databaseBuilder(
                App.appInstance,
                NotesDatabase::class.java,
                DB_NAME
            ).build()
        }
    }
}