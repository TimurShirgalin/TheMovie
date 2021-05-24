package com.example.themovie.ui.details

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.example.themovie.R
import com.example.themovie.databinding.FragmentMovieDetailsBinding
import com.example.themovie.model.Movies
import com.example.themovie.model.MoviesWithoutGenres
import com.example.themovie.model.NotesData
import com.example.themovie.ui.main.MainActivity
import com.example.themovie.util.SharedPref
import com.example.themovie.viewModel.MainViewModel

const val DETAILS_FRAGMENT_BROADCAST = "BROADCAST_EXTRA"
const val INTENT_FILTER = "INTENT_FILTER"

class MovieDetails : Fragment() {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var noteData: NotesData
    private var like: Boolean = false
    private var likeFromDB: Boolean = false
    private lateinit var movie: MoviesWithoutGenres

    private val dataReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @RequiresApi(Build.VERSION_CODES.N)
        override fun onReceive(context: Context?, intent: Intent?) = with(binding) {
            intent?.getParcelableExtra<MoviesWithoutGenres>(DETAILS_FRAGMENT_BROADCAST).let {
                if (it != null && context != null) {
                    Glide.with(context)
                        .asBitmap()
                        .load("https://image.tmdb.org/t/p/w185_and_h278_bestv2${it.poster_path}")
                        .into(detailsImage)
                    detailsTitle.text = it.title
                    detailsDescription.text = it.overview
                    detailsAverage.text = it.vote_average.toString()
                    movie = it
                    setFab()
                    model.getMovieData(it.id)
                    model.getNoteData(it.id)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        model.apply {
            getLiveMovieData().observe(viewLifecycleOwner, { renderData(it) })
            getLiveNoteData().observe(viewLifecycleOwner, { viewNoteData(it) })
        }
        ServiceMovieData.start(
            requireContext(),
            Intent(context, ServiceMovieData::class.java).putExtra(
                SERVICE_DATA_INT_EXTRA, arguments?.getParcelable<Movies>(KEY)?.id
            )
        )
    }

    private fun viewNoteData(data: NotesData) {
        noteData = data
        requireActivity().findViewById<Toolbar>(R.id.toolbar).menu.findItem(R.id.app_bar_notes)
            .let {
                it?.setIcon(
                    if (noteData.note == "") R.drawable.ic_notes
                    else R.drawable.ic_notes_filled
                )
            }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun renderData(movieData: MoviesWithoutGenres) {
        val fabContext = activity as MainActivity
        movieData.like.let {
            likeFromDB = movieData.like
            like = movieData.like
            binding.fabLike.setImageDrawable(
                ContextCompat.getDrawable(
                    fabContext,
                    if (movieData.like) R.drawable.ic_like_true
                    else R.drawable.ic_like_false
                )
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_notes -> {
                NoteFragment(model, noteData).show(
                    requireActivity().supportFragmentManager, "MyCustomFragment"
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setFab() = with(binding) {
        val fabContext = activity as MainActivity
        fabLike.setOnClickListener {
            like = !like
            fabLike.setImageDrawable(
                ContextCompat.getDrawable(
                    fabContext,
                    if (like) R.drawable.ic_like_true
                    else R.drawable.ic_like_false
                )
            )
        }
    }

    companion object {
        const val KEY = "movieDetails"
        private lateinit var model: MainViewModel

        fun newInstance(bundle: Bundle, viewModel: MainViewModel): MovieDetails {
            val fragment = MovieDetails()
            model = viewModel
            fragment.arguments = bundle
            return fragment
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onDestroyView() {
        _binding = null
        (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        context?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(dataReceiver)
        }
        if (likeFromDB != like) {
            if (like) model.saveToDB(movie)
            else model.deleteMovieFromDB(movie.id!!)
        }
        super.onDestroyView()
    }
}