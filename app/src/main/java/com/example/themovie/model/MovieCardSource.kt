package com.example.themovie.model

interface MovieCardSource {
    fun getDataFromLocalSource(): List<GenresData>
}