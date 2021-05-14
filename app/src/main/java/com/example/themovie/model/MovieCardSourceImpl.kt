package com.example.themovie.model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData

class MovieCardSourceImpl : MovieCardSource {
    override fun getDataFromLocalSource(): List<GenresDataLocal> = GetData().getGenresData()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun getDataFromOutSource(liveDataToObserve: MutableLiveData<AppStateMovies>): List<Categories> {
        return loadDataMovies(liveDataToObserve)
    }
}