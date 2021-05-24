package com.example.themovie.model

import androidx.lifecycle.MutableLiveData

interface MovieCardSource {
    fun getDataFromLocalSource(): List<GenresDataLocal>
    fun getDataFromOutSource(liveDataToObserve: MutableLiveData<AppStateMovies>, average: Int)
    fun getAllFavoriteMovies(): List<MoviesWithoutGenres>
    fun saveFavoriteMovie(movie : MoviesWithoutGenres)
    fun deleteMovieFromFavorite(movieId: Int)
    fun getMovieData(movieId: Int?) : MoviesWithoutGenres
    fun getNote(movieId: Int?) : NotesData
    fun deleteNote(movieId: Int?)
    fun saveNote(note: NotesData)
}