package com.example.themovie.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.themovie.R
import com.example.themovie.model.GenresData

class GenresAdapter : RecyclerView.Adapter<GenresAdapter.ViewHolder>() {

    private var genresData: List<GenresData> = listOf()

    fun setGenresData(data: List<GenresData>) {
        genresData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v: View = LayoutInflater.from(viewGroup.context).inflate(
            R.layout.fragment_genres, viewGroup, false
        )
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) =
        viewHolder.bind(genresData[position])

    override fun getItemCount() = genresData.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var genreTextView: TextView = itemView.findViewById(R.id.textView_genre)

        fun bind(genreData: GenresData) {
            genreTextView.text = genreData.genres
            setMovieDataRecycler(genreData)
        }

        private fun setMovieDataRecycler(moviesData: GenresData) {
            val movieDataRecycler: RecyclerView = itemView.findViewById(R.id.recycler_movieData)
            val movieDataLayoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            val movieDataAdapter = MovieAdapter()

            movieDataRecycler.layoutManager = movieDataLayoutManager
            movieDataRecycler.adapter = movieDataAdapter
            movieDataAdapter.setMovieData(moviesData.movieData)
        }
    }
}