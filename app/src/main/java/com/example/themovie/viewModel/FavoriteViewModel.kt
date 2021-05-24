package com.example.themovie.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.themovie.model.AppStateFavoriteMovies
import com.example.themovie.model.MovieCardSourceImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class FavoriteViewModel(private val liveDataToObserve: MutableLiveData<AppStateFavoriteMovies> = MutableLiveData()) :
    ViewModel(), CoroutineScope by MainScope() {

    private val movieCardSourceImpl = MovieCardSourceImpl()

    fun getData() {
        liveDataToObserve.value = AppStateFavoriteMovies.Loading
        launch(Dispatchers.IO) {
            liveDataToObserve.postValue(AppStateFavoriteMovies.Success(movieCardSourceImpl.getAllFavoriteMovies()))
        }
    }

    fun getLiveData() = liveDataToObserve
}