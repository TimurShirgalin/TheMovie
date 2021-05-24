package com.example.themovie.ui.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.themovie.R
import com.example.themovie.model.MoviesWithoutGenres

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    private var movies: List<MoviesWithoutGenres> = listOf()

    fun setGenresData(data: List<MoviesWithoutGenres>) {
        movies = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteAdapter.ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.fragment_favorite_card, parent, false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteAdapter.ViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int = movies.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val movieTitle = itemView.findViewById<TextView>(R.id.favorite_movieTitle)
        private val movieImage = itemView.findViewById<ImageView>(R.id.favorite_imageView)

        fun bind(movies: MoviesWithoutGenres) {
            movieTitle.text = movies.title
            Glide.with(itemView)
                .asBitmap()
                .load("https://image.tmdb.org/t/p/w185_and_h278_bestv2${movies.poster_path}")
                .into(movieImage)
        }
    }
}