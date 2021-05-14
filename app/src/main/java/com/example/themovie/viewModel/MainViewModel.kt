package com.example.themovie.viewModel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.themovie.model.AppStateMovies
import com.example.themovie.model.MovieCardSourceImpl

@RequiresApi(Build.VERSION_CODES.N)
class MainViewModel(private val liveDataToObserve: MutableLiveData<AppStateMovies> = MutableLiveData()) :
    ViewModel() {

    init {
        liveDataToObserve.value = AppStateMovies.Loading
        MovieCardSourceImpl().getDataFromOutSource(liveDataToObserve)
    }

    fun getLiveData() = liveDataToObserve
}