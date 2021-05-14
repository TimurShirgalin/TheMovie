package com.example.themovie.model

import androidx.lifecycle.MutableLiveData

interface MovieCardSource {
    fun getDataFromLocalSource(): List<GenresDataLocal>
    fun getDataFromOutSource(liveDataToObserve: MutableLiveData<AppStateMovies>): List<Categories>
}