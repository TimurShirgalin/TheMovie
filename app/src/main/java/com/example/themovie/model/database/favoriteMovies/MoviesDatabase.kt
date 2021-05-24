package com.example.themovie.model.database.favoriteMovies

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.themovie.util.App

@Database(entities = [MoviesEntity::class], version = 1, exportSchema = false)
abstract class MoviesDatabase : RoomDatabase() {
    abstract fun moviesDAO(): MoviesDao

    companion object {
        private const val DB_NAME = "movie_database.db"
        val database: MoviesDatabase by lazy {
            Room.databaseBuilder(
                App.appInstance,
                MoviesDatabase::class.java,
                DB_NAME
            ).build()
        }
    }
}