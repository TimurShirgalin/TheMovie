package com.example.themovie.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.themovie.R
import com.example.themovie.model.Categories

class GenresAdapter(private val manager: FragmentManager?) : RecyclerView.Adapter<GenresAdapter.ViewHolder>() {

    private var genresData: List<Categories> = listOf()

    fun setGenresData(data: List<Categories>) {
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

        fun bind(genreData: Categories) {
            genreTextView.text = genreData.categoryName
            setMovieDataRecycler(genreData)
        }

        private fun setMovieDataRecycler(moviesData: Categories) {
            val movieDataRecycler: RecyclerView = itemView.findViewById(R.id.recycler_movieData)
            val movieDataLayoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            val movieDataAdapter = MovieAdapter()

            movieDataRecycler.layoutManager = movieDataLayoutManager
            movieDataRecycler.adapter = movieDataAdapter
            movieDataAdapter.setMovieData(moviesData.movieList)

            movieDataAdapter.setOnItemClickListener(object : MovieAdapter.OnItemClickListener {
                override fun onItemClick(view: View?, position: Int) {
                    if (manager != null) {
                        val bundle = Bundle()
                        bundle.putParcelable(MovieDetails.KEY, moviesData.movieList[position])
                        manager.beginTransaction()
                            .add(R.id.container, MovieDetails.newInstance(bundle))
                            .addToBackStack(null)
                            .commit()
                    }
                }
            })
        }
    }
}