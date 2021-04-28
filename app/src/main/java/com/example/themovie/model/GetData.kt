package com.example.themovie.model

import com.example.themovie.R

class GetData {

    fun getGenresData(): MutableList<GenresData> {
        val genresData: MutableList<GenresData> = mutableListOf()
        for (uniqueGenre in getUniqueGenres()) {
            val movies: MutableList<MovieData> = mutableListOf()
            for (movieDatum in getMovieData()) {
                if (movieDatum.genre == uniqueGenre) {
                    movies.add(movieDatum)
                }
            }
            genresData.add(GenresData(uniqueGenre, movies))
        }
        return genresData
    }

    private fun getUniqueGenres(): MutableList<String> {
        val movieGenres: MutableList<String> = mutableListOf()
        for (movieDatum in getMovieData()) {
            if (!movieGenres.contains(movieDatum.genre)) {
                movieGenres.add(movieDatum.genre)
            }
        }
        return movieGenres
    }

    private fun getMovieData(): List<MovieData> {
        return listOf(
            MovieData("movie1", "Комедия", "Описание1", R.drawable.movie_pic1),
            MovieData("movie2", "Боевик", "Описание2", R.drawable.movie_pic2),
            MovieData("movie3", "Комедия", "Описание3", R.drawable.movie_pic3),
            MovieData("movie4", "Боевик", "Описание4", R.drawable.movie_pic4),
            MovieData("movie5", "Триллер", "Описание5", R.drawable.movie_pic5),
            MovieData("movie6", "Комедия", "Описание6", R.drawable.movie_pic6),
            MovieData("movie7", "Драма", "Описание7", R.drawable.movie_pic7),
            MovieData("movie8", "Комедия", "Описание8", R.drawable.movie_pic8),
            MovieData("movie9", "Боевик", "Описание9", R.drawable.movie_pic9),
            MovieData("movie10", "Комедия", "Описание10", R.drawable.movie_pic10),
            MovieData("movie11", "Боевик", "Описание11", R.drawable.movie_pic11),
            MovieData("movie12", "Ужасы", "Описание12", R.drawable.movie_pic12),
            MovieData("movie13", "Боевик", "Описание13", R.drawable.movie_pic13),
            MovieData("movie14", "Триллер", "Описание14", R.drawable.movie_pic14),
            MovieData("movie15", "Комедия", "Описание15", R.drawable.movie_pic15),
            MovieData("movie16", "Драма", "Описание16", R.drawable.movie_pic16),
            MovieData("movie17", "Триллер", "Описание17", R.drawable.movie_pic17),
            MovieData("movie18", "Боевик", "Описание18", R.drawable.movie_pic18),
            MovieData("movie19", "Драма", "Описание19", R.drawable.movie_pic19),
            MovieData("movie20", "Триллер", "Описание20", R.drawable.movie_pic20),
            MovieData("movie21", "Комедия", "Описание21", R.drawable.movie_pic21)
        )
    }
}