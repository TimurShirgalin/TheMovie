package com.example.themovie.model

import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.*
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

private const val YOUR_API_KEY = "751f0ad3d019a2d30a59aa7c461351dc"

private const val numberOfLoadPages: Int = 2
private const val genresURL: String =
    "https://api.tmdb.org/3/genre/movie/list?api_key=${YOUR_API_KEY}&language=ru-RU"
private const val moviesURL: String =
    "https://api.tmdb.org/3/discover/movie?api_key=${YOUR_API_KEY}&language=ru-RU"
private val genresList: MutableList<Genres> = mutableListOf()
private val moviesData: MutableList<Movies> = mutableListOf()
private val genresListUnique: MutableList<Int> = mutableListOf()
private val categoriesData: MutableList<Categories> = mutableListOf()

@RequiresApi(Build.VERSION_CODES.N)
fun loadDataMovies(liveDataToObserve: MutableLiveData<AppStateMovies>): MutableList<Categories> {
    val handler = Handler(Looper.getMainLooper())
    Thread(Runnable {
        try {
            var urlConnection: HttpsURLConnection? = null
            try {
                urlConnection = URL(genresURL).openConnection() as HttpsURLConnection
                urlConnection.requestMethod = "GET"
                urlConnection.readTimeout = 3000
                urlConnection.connectTimeout = 3000
                val bufferedReader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                Gson().fromJson(getLines(bufferedReader),
                    GenresData::class.java).genres.forEach {
                    genresList.add(it)
                }
            } catch (e: Exception) {
                Log.e("", "Fail connection", e)
                e.printStackTrace()
                //Обработка ошибки
            } finally {
                urlConnection?.disconnect()
            }
            try {
                for (i in 1..numberOfLoadPages) {
                    urlConnection = URL("$moviesURL&page=$i").openConnection() as HttpsURLConnection
                    urlConnection.requestMethod = "GET"
                    urlConnection.readTimeout = 3000
                    urlConnection.connectTimeout = 3000
                    val bufferedReader =
                        BufferedReader(InputStreamReader(urlConnection.inputStream))
                    Gson().fromJson(getLines(bufferedReader),
                        MoviesData::class.java).results.forEach {
                        moviesData.add(it)
                        it.genre_ids?.forEach { idGenre: Int ->
                            if (!genresListUnique.contains(idGenre)) {
                                genresListUnique.add(idGenre)
                            }
                        }
                    }
                }
                genresListUnique.forEach { uniqueGenre: Int ->
                    val movies: MutableList<Movies> = mutableListOf()
                    moviesData.forEach {
                        if (it.genre_ids!!.contains(uniqueGenre)) {
                            movies.add(it)
                        }
                    }
                    val text = genresList.firstOrNull { it.id == uniqueGenre }!!.name
                    categoriesData.add(Categories(
                        uniqueGenre,
                        text?.substring(0, 1)?.toUpperCase(Locale.ROOT) + text?.substring(1),
                        movies))
                }
                handler.post { liveDataToObserve.postValue(AppStateMovies.Success(categoriesData)) }
            } catch (e: Exception) {
                Log.e("", "Fail connection", e)
                e.printStackTrace()
                //Обработка ошибки
            } finally {
                urlConnection?.disconnect()
            }
        } catch (e: MalformedURLException) {
            Log.e("", "Fail URI", e)
            e.printStackTrace()
            //Обработка ошибки
        }
    }).start()
    return categoriesData
}

@RequiresApi(Build.VERSION_CODES.N)
private fun getLines(reader: BufferedReader): String {
    return reader.lines().collect(Collectors.joining("\n"))
}