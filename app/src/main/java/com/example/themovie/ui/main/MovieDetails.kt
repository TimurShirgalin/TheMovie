package com.example.themovie.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.themovie.databinding.FragmentMovieDetailsBinding
import com.example.themovie.model.Movies

class MovieDetails : Fragment() {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val movieData = arguments?.getParcelable<Movies>(KEY)

        if (movieData != null) {
            Glide.with(context!!)
                .asBitmap()
                .load("https://image.tmdb.org/t/p/w185_and_h278_bestv2${movieData.poster_path}")
                .into(binding.detailsImage)
            binding.detailsTitle.text = "Название:\n${movieData.title}"
            binding.detailsCategory.text = "Описание:\n${movieData.overview}"
            binding.detailsDescription.text = "Рейтинг:\n${movieData.vote_average.toString()}"
        }
    }

    companion object {
        const val KEY = "movieDetails"

        fun newInstance(bundle: Bundle): MovieDetails {
            val fragment = MovieDetails()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        activity?.supportFragmentManager?.popBackStack()
        (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }
}