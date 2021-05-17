package com.example.themovie.model.retrofit

import androidx.lifecycle.MutableLiveData
import com.example.themovie.model.*
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

private const val BASE_URL: String = "https://api.tmdb.org/"

class LoadDataMoviesFromRetrofit {
    private val genres = startRetrofit().create(GenresAPI::class.java)
    private val movies = startRetrofit().create(MoviesAPI::class.java)

    fun getGenresData(language: String, liveDataToObserve: MutableLiveData<AppStateMovies>) {
        genres.getGenres(YOUR_API_KEY, language).enqueue(object :
            Callback<GenresData> {
            override fun onResponse(call: Call<GenresData>, response: Response<GenresData>) =
                getMoviesData(language, response.body()!!.genres, liveDataToObserve)

            override fun onFailure(call: Call<GenresData>, t: Throwable) =
                liveDataToObserve.postValue(AppStateMovies.Error(Throwable("Ошибка сервера")))
        })
    }

    fun getMoviesData(
        language: String, genres: List<Genres>, liveDataToObserve: MutableLiveData<AppStateMovies>,
    ) {
        movies.getMovies(YOUR_API_KEY, language).enqueue(object :
            Callback<MoviesData> {
            override fun onResponse(call: Call<MoviesData>, response: Response<MoviesData>) {
                val categoriesData: MutableList<Categories> = mutableListOf()
                genres.forEach {
                    val movies: MutableList<Movies> = mutableListOf()
                    response.body()!!.results.forEach { movie: Movies ->
                        if (movie.genre_ids!!.contains(it.id)) movies.add(movie)
                    }
                    val text = it.name
                    if (movies.isNotEmpty()) categoriesData.add(
                        Categories(
                            it.id,
                            text?.substring(0, 1)?.toUpperCase(Locale.ROOT) + text?.substring(1),
                            movies
                        )
                    )
                }
                liveDataToObserve.postValue(AppStateMovies.Success(categoriesData))
            }

            override fun onFailure(call: Call<MoviesData>, t: Throwable) =
                liveDataToObserve.postValue(AppStateMovies.Error(Throwable("Ошибка сервера")))
        })
    }

    private fun startRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
    }
}