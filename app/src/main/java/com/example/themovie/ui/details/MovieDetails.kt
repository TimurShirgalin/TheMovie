package com.example.themovie.ui.details

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.example.themovie.R
import com.example.themovie.databinding.FragmentMovieDetailsBinding
import com.example.themovie.model.Movies
import com.example.themovie.model.MoviesWithoutGenres

const val DETAILS_FRAGMENT_BROADCAST = "BROADCAST_EXTRA"
const val INTENT_FILTER = "INTENT_FILTER"

class MovieDetails : Fragment() {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!
    private var movieData: MoviesWithoutGenres? = null

    private val dataReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @RequiresApi(Build.VERSION_CODES.N)
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.getParcelableExtra<MoviesWithoutGenres>(DETAILS_FRAGMENT_BROADCAST).let {
                movieData = it
                if (movieData != null && context != null) {
                    Glide.with(context)
                        .asBitmap()
                        .load("https://image.tmdb.org/t/p/w185_and_h278_bestv2${movieData!!.poster_path}")
                        .into(binding.detailsImage)
                    binding.apply {
                        detailsTitle.text =
                            resources.getString(R.string.moveTitle) + movieData!!.title
                        detailsCategory.text =
                            resources.getString(R.string.description) + movieData!!.overview
                        detailsDescription.text =
                            resources.getString(R.string.average) + movieData!!.vote_average.toString()
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            LocalBroadcastManager.getInstance(it)
                .registerReceiver(dataReceiver, IntentFilter(INTENT_FILTER))
        }
    }

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

        ServiceMovieData.start(
            requireContext(),
            Intent(context, ServiceMovieData::class.java).putExtra(
                SERVICE_DATA_INT_EXTRA, arguments?.getParcelable<Movies>(KEY)?.id
            )
        )
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
        _binding = null
        (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        context?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(dataReceiver)
        }
        super.onDestroyView()
    }
}