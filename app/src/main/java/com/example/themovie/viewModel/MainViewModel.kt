package com.example.themovie.viewModel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.themovie.model.AppStateMovies
import com.example.themovie.model.MovieCardSourceImpl
import com.example.themovie.model.MoviesWithoutGenres
import com.example.themovie.model.NotesData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.N)
class MainViewModel(
    private val liveMovieCategories: MutableLiveData<AppStateMovies> = MutableLiveData(),
    private val liveMoveData: MutableLiveData<MoviesWithoutGenres> = MutableLiveData(),
    private val liveNoteData: MutableLiveData<NotesData> = MutableLiveData(),
) :
    ViewModel(), CoroutineScope by MainScope() {

    private val movieCardSourceImpl = MovieCardSourceImpl()

    init {
        initData(basicAverage)
    }

    private fun initData(average: Int) {
        liveMovieCategories.value = AppStateMovies.Loading
        movieCardSourceImpl.getDataFromOutSource(liveMovieCategories, average)
    }

    fun getMoviesData(average: Int) = initData(average)

    fun getMovieData(movieId: Int?) = launch(Dispatchers.IO) {
        liveMoveData.postValue(movieCardSourceImpl.getMovieData(movieId))
    }

    fun deleteMovieFromDB(movieId: Int) = launch(Dispatchers.IO) {
        movieCardSourceImpl.deleteMovieFromFavorite(movieId)
    }

    fun saveToDB(movie: MoviesWithoutGenres) =
        launch(Dispatchers.IO) { movieCardSourceImpl.saveFavoriteMovie(movie) }

    fun saveNote(note: NotesData) = launch(Dispatchers.IO) {
        movieCardSourceImpl.saveNote(note)
        liveNoteData.postValue(note)
    }

    fun getNoteData(movieId: Int?) =
        launch(Dispatchers.IO) { liveNoteData.postValue(movieCardSourceImpl.getNote(movieId)) }

    fun deleteNote(movieId: Int?) =
        launch(Dispatchers.IO) {
            movieCardSourceImpl.deleteNote(movieId)
            liveNoteData.postValue(NotesData(basicId, basicNote))
        }

    fun getLiveNoteData() = liveNoteData

    fun getLiveCategoriesData() = liveMovieCategories

    fun getLiveMovieData() = liveMoveData

    companion object {
        private const val basicAverage: Int = 0
        private const val basicNote: String = ""
        private const val basicId: Int = 0
    }
}