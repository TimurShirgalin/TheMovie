package com.example.themovie.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.themovie.model.AppStateMovies
import com.example.themovie.model.MovieCardSourceImpl

class MainViewModel(private val liveDataToObserve: MutableLiveData<AppStateMovies> = MutableLiveData()) :
    ViewModel() {

    fun getLiveData() = liveDataToObserve

    fun getData() {
        liveDataToObserve.value = AppStateMovies.Loading
        Thread {
            Thread.sleep(3000)
            liveDataToObserve.postValue(AppStateMovies.Success(MovieCardSourceImpl().getDataFromLocalSource()))
        }.start()
    }
}