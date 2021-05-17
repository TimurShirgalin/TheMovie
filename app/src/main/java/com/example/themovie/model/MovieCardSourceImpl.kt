package com.example.themovie.model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.example.themovie.model.retrofit.LoadDataMoviesFromRetrofit

private const val LANGUAGE: String = "ru-RU"

class MovieCardSourceImpl : MovieCardSource {
    override fun getDataFromLocalSource(): List<GenresDataLocal> = GetData().getGenresData()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun getDataFromOutSource(liveDataToObserve: MutableLiveData<AppStateMovies>) =
        LoadDataMoviesFromRetrofit().getGenresData(LANGUAGE, liveDataToObserve)
}