package com.example.themovie.ui.main

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

class MovieDetails : Fragment() {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!
    private var movieData: MoviesWithoutGenres? = null

    private val dataReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        @RequiresApi(Build.VERSION_CODES.N)
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.getParcelableExtra<MoviesWithoutGenres>(DETAILS_FRAGMENT_BROADCAST).let {
                movieData = it
                if (movieData != null) {
                    Glide.with(context)
                        .asBitmap()
                        .load("https://image.tmdb.org/t/p/w185_and_h278_bestv2${movieData!!.poster_path}")
                        .into(binding.detailsImage)
                    binding.detailsTitle.text =
                        resources?.getString(R.string.moveTitle) + movieData!!.title
                    binding.detailsCategory.text =
                        resources?.getString(R.string.description) + movieData!!.overview
                    binding.detailsDescription.text =
                        resources?.getString(R.string.average) + movieData!!.vote_average.toString()
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
//        val movieData = arguments?.getParcelable<Movies>(KEY)
        context?.let {
            it.startService(Intent(it, ServiceData::class.java).apply {
                putExtra(SERVICE_DATA_INT_EXTRA, arguments?.getParcelable<Movies>(KEY)?.id)
            })
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
        _binding = null
        activity?.supportFragmentManager?.popBackStack()
        (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        context?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(dataReceiver)
        }
        super.onDestroyView()
    }
}