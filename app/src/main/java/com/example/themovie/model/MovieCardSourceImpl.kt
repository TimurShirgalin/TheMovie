package com.example.themovie.model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.example.themovie.model.database.favoriteMovies.MoviesDatabase
import com.example.themovie.model.database.favoriteMovies.MoviesEntity
import com.example.themovie.model.database.notes.NotesDatabase
import com.example.themovie.model.database.notes.NotesEntity
import com.example.themovie.model.retrofit.LoadDataMoviesFromRetrofit

class MovieCardSourceImpl : MovieCardSource {
    override fun getDataFromLocalSource(): List<GenresDataLocal> = GetData().getGenresData()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun getDataFromOutSource(
        liveDataToObserve: MutableLiveData<AppStateMovies>,
        average: Int,
    ) = LoadDataMoviesFromRetrofit().getGenresData(LANGUAGE, liveDataToObserve, average)

    override fun getAllFavoriteMovies(): List<MoviesWithoutGenres> {
        val favoriteMovies: MutableList<MoviesWithoutGenres> = mutableListOf()
        convertListEntityToListMovies(MoviesDatabase.database.moviesDAO().all()).forEach {
            if (it.like) favoriteMovies.add(it)
        }
        return favoriteMovies
    }

    override fun saveFavoriteMovie(movie: MoviesWithoutGenres) =
        MoviesDatabase.database.moviesDAO().insert(convertMoviesToEntity(movie))

    override fun deleteMovieFromFavorite(movieId: Int) {
        MoviesDatabase.database.moviesDAO().deleteByMovieId(movieId)
    }

    override fun getMovieData(movieId: Int?): MoviesWithoutGenres =
        convertEntityToMovie(MoviesDatabase.database.moviesDAO().getMovieData(movieId))

    override fun getNote(movieId: Int?): NotesData =
        NotesDatabase.database.notesDAO().getNote(movieId).let {
            if (it != null) NotesData(it.movieId, it.note)
            else NotesData(movieId, basicNote)
        }

    override fun deleteNote(movieId: Int?) = NotesDatabase.database.notesDAO().deleteNote(movieId)

    override fun saveNote(note: NotesData) =
        NotesDatabase.database.notesDAO().addNote(NotesEntity(note.id, note.note))

    private fun convertEntityToMovie(entity: MoviesEntity?): MoviesWithoutGenres =
        entity.let {
            if (it != null) {
                MoviesWithoutGenres(
                    it.movieId,
                    it.title,
                    it.overview,
                    it.poster_path,
                    it.vote_average,
                    it.like
                )
            } else {
                MoviesWithoutGenres(0, null, null, null, null, false)
            }
        }

    private fun convertListEntityToListMovies(entityList: List<MoviesEntity>): List<MoviesWithoutGenres> =
        entityList.map {
            MoviesWithoutGenres(
                it.movieId,
                it.title,
                it.overview,
                it.poster_path,
                it.vote_average,
                it.like
            )
        }

    private fun convertMoviesToEntity(movie: MoviesWithoutGenres): MoviesEntity = MoviesEntity(
        movie.id,
        movie.title,
        movie.overview,
        movie.poster_path,
        movie.vote_average?.toDouble(),
        true
    )

    companion object {
        private const val LANGUAGE: String = "ru-RU"
        private const val basicNote: String = ""
    }
}