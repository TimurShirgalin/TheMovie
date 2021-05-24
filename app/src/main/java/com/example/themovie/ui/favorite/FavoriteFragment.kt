package com.example.themovie.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.themovie.databinding.FragmentFavoriteMoviesBinding
import com.example.themovie.model.AppStateFavoriteMovies
import com.example.themovie.model.MoviesWithoutGenres
import com.example.themovie.viewModel.FavoriteViewModel

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteMoviesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoriteViewModel by lazy {
        ViewModelProvider(this).get(FavoriteViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFavoriteMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        viewModel.getLiveData().observe(viewLifecycleOwner, { renderData(it) })
        viewModel.getData()
    }

    private fun renderData(data: AppStateFavoriteMovies?) = with(binding) {
        when (data) {
            is AppStateFavoriteMovies.Success -> {
//                loadingLayout.visibility = View.GONE
                val recyclerView: RecyclerView = recyclerFavorite
                initRecyclerView(recyclerView, data.data)
            }
            is AppStateFavoriteMovies.Error -> {
//                loadingLayout.visibility = View.GONE
//                Snackbar
//                    .make(recyclerMovieGenres, "Error", Snackbar.LENGTH_INDEFINITE)
//                    .setAction("Reload") { viewModel.getMoviesData(MainFragment.averageBasic) }
//                    .show()
            }
            is AppStateFavoriteMovies.Loading -> {
//                loadingLayout.visibility = View.VISIBLE
            }
        }
    }

    private fun initRecyclerView(recyclerView: RecyclerView, data: List<MoviesWithoutGenres>) {
        recyclerView.setHasFixedSize(true)
        val adapter = FavoriteAdapter()
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        adapter.setGenresData(data)
    }

    companion object {

        fun newInstance() = FavoriteFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}