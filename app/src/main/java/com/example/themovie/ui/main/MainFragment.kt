package com.example.themovie.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.themovie.databinding.MainFragmentBinding
import com.example.themovie.model.AppStateMovies
import com.example.themovie.model.GenresData
import com.example.themovie.viewModel.MainViewModel
import com.google.android.material.snackbar.Snackbar

class MainFragment : Fragment() {
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLiveData().observe(viewLifecycleOwner, { renderData(it) })
        viewModel.getData()
    }

    private fun renderData(data: AppStateMovies?) {
        when (data) {
            is AppStateMovies.Success -> {
                binding.loadingLayout.visibility = View.GONE
                val recyclerView: RecyclerView = binding.recyclerMovieGenres
                initRecyclerView(recyclerView, data.data)
            }
            is AppStateMovies.Error -> {
                binding.loadingLayout.visibility = View.GONE
                Snackbar
                    .make(binding.recyclerMovieGenres, "Error", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Reload") { viewModel.getData() }
                    .show()
            }
            is AppStateMovies.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }
        }
    }

    private fun initRecyclerView(recyclerView: RecyclerView, data: List<GenresData>) {
        recyclerView.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(context)
        val adapter = GenresAdapter()

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        adapter.setGenresData(data)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}