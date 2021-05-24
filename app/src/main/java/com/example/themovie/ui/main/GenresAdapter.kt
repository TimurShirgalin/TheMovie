package com.example.themovie.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.themovie.R
import com.example.themovie.model.Categories

class GenresAdapter : RecyclerView.Adapter<GenresAdapter.ViewHolder>() {

    private var genresData: List<Categories> = listOf()
    private lateinit var setMovieAdapter: OnSetMovieAdapter

    fun setGenresData(data: List<Categories>) {
        genresData = data
        notifyDataSetChanged()
    }

    interface OnSetMovieAdapter {
        fun setMovieAdapter(itemView: View, moviesData: Categories)
    }

    fun setOnMovieAdapter(onSetMovieAdapter: OnSetMovieAdapter) {
        setMovieAdapter = onSetMovieAdapter
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v: View = LayoutInflater.from(viewGroup.context).inflate(
            R.layout.fragment_genres, viewGroup, false
        )
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) =
        viewHolder.bind(genresData[position], setMovieAdapter)

    override fun getItemCount() = genresData.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var genreTextView: TextView = itemView.findViewById(R.id.textView_genre)

        fun bind(genreData: Categories, setMovieAdapter: OnSetMovieAdapter) {
            setMovieAdapter.setMovieAdapter(itemView, genreData)
            genreTextView.text = genreData.categoryName
        }
    }
}