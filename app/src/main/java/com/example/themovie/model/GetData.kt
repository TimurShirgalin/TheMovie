package com.example.themovie.model

import com.example.themovie.R

class GetData {

    fun getGenresData(): MutableList<GenresDataLocal> {
        val genresData: MutableList<GenresDataLocal> = mutableListOf()
        for (uniqueGenre in getUniqueGenres()) {
            val movies: MutableList<MovieDataLocal> = mutableListOf()
            for (movieDatum in getMovieData()) {
                if (movieDatum.genre == uniqueGenre) {
                    movies.add(movieDatum)
                }
            }
            genresData.add(GenresDataLocal(uniqueGenre, movies))
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

    private fun getMovieData(): List<MovieDataLocal> {
        return listOf(
            MovieDataLocal("movie1", "Комедия", "Описание1", R.drawable.movie_pic1),
            MovieDataLocal("movie2", "Боевик", "Описание2", R.drawable.movie_pic2),
            MovieDataLocal("movie3", "Комедия", "Описание3", R.drawable.movie_pic3),
            MovieDataLocal("movie4", "Боевик", "Описание4", R.drawable.movie_pic4),
            MovieDataLocal("movie5", "Триллер", "Описание5", R.drawable.movie_pic5),
            MovieDataLocal("movie6", "Комедия", "Описание6", R.drawable.movie_pic6),
            MovieDataLocal("movie7", "Драма", "Описание7", R.drawable.movie_pic7),
            MovieDataLocal("movie8", "Комедия", "Описание8", R.drawable.movie_pic8),
            MovieDataLocal("movie9", "Боевик", "Описание9", R.drawable.movie_pic9),
            MovieDataLocal("movie10", "Комедия", "Описание10", R.drawable.movie_pic10),
            MovieDataLocal("movie11", "Боевик", "Описание11", R.drawable.movie_pic11),
            MovieDataLocal("movie12", "Ужасы", "Описание12", R.drawable.movie_pic12),
            MovieDataLocal("movie13", "Боевик", "Описание13", R.drawable.movie_pic13),
            MovieDataLocal("movie14", "Триллер", "Описание14", R.drawable.movie_pic14),
            MovieDataLocal("movie15", "Комедия", "Описание15", R.drawable.movie_pic15),
            MovieDataLocal("movie16", "Драма", "Описание16", R.drawable.movie_pic16),
            MovieDataLocal("movie17", "Триллер", "Описание17", R.drawable.movie_pic17),
            MovieDataLocal("movie18", "Боевик", "Описание18", R.drawable.movie_pic18),
            MovieDataLocal("movie19", "Драма", "Описание19", R.drawable.movie_pic19),
            MovieDataLocal("movie20", "Триллер", "Описание20", R.drawable.movie_pic20),
            MovieDataLocal("movie21", "Комедия", "Описание21", R.drawable.movie_pic21)
        )
    }
}