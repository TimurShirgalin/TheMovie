package com.example.themovie.model.database.favoriteMovies

import androidx.room.*

@Dao
interface MoviesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: MoviesEntity)

    @Update
    fun update(entity: MoviesEntity)

    @Delete
    fun delete(entity: MoviesEntity)

    @Query("DELETE FROM MoviesEntity WHERE movieId LIKE :movieId")
    fun deleteByMovieId(movieId: Int?)

    @Query("SELECT * FROM MoviesEntity")
    fun all(): List<MoviesEntity>

    @Query("SELECT * FROM MoviesEntity WHERE movieId LIKE :movieId")
    fun getMovieData(movieId: Int?): MoviesEntity?
}