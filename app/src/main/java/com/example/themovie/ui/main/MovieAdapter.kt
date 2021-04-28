package com.example.themovie.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.themovie.R
import com.example.themovie.model.MovieData

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    private var movieData: List<MovieData> = listOf()

    fun setMovieData(data: List<MovieData>) {
        movieData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(
            R.layout.fragment_movies, parent, false
        )
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(movieData[position])

    override fun getItemCount() = movieData.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var movie: TextView = itemView.findViewById(R.id.textView_movieTitle)
        private var image: AppCompatImageView = itemView.findViewById(R.id.imageView)

        fun bind(movieData: MovieData) {
            movie.text = movieData.movieName
            image.setImageResource(movieData.image)
        }
    }
}