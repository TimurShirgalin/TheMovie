package com.example.themovie.ui.main

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.themovie.R
import com.example.themovie.databinding.MainFragmentBinding
import com.example.themovie.model.AppStateMovies
import com.example.themovie.model.Categories
import com.example.themovie.ui.details.MovieAdapter
import com.example.themovie.ui.details.MovieDetails
import com.example.themovie.ui.favorite.FavoriteFragment
import com.example.themovie.util.IS_CHECKED
import com.example.themovie.util.IS_MAIN
import com.example.themovie.util.NOTE_ICON
import com.example.themovie.util.SharedPref
import com.example.themovie.viewModel.MainViewModel
import com.google.android.material.snackbar.Snackbar

class MainFragment : Fragment() {
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!
    private val connection = Connect { Toast.makeText(context, "$it", Toast.LENGTH_LONG).show() }
    private lateinit var sharedPref: SharedPref
    private val adapter = GenresAdapter()
    private var countFavoritesInDB: Int? = null

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = SharedPref(requireActivity())
        setHasOptionsMenu(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            (context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).registerDefaultNetworkCallback(
                object :
                    ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) = makeToast(true)
                    override fun onLost(network: Network) = makeToast(false)
                })
        } else {
            context?.let {
                LocalBroadcastManager.getInstance(it)
                    .registerReceiver(
                        connection,
                        IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
                    )
            }
        }
    }

    private fun makeToast(status: Boolean) =
        Toast.makeText(context, "$status", Toast.LENGTH_LONG).show()

    companion object {
        fun newInstance() = MainFragment()
        private const val averageBasic = 0
        private const val averageMin = 8
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLiveCategoriesData().observe(viewLifecycleOwner, { renderData(it) })
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun renderData(data: AppStateMovies?) = with(binding) {
        when (data) {
            is AppStateMovies.Success -> {
                loadingLayout.visibility = View.GONE
                val recyclerView: RecyclerView = recyclerMovieGenres
                initRecyclerView(recyclerView, data.data)
            }
            is AppStateMovies.Error -> {
                loadingLayout.visibility = View.GONE
                Snackbar
                    .make(recyclerMovieGenres, "Error", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Reload") { viewModel.getMoviesData(averageBasic) }
                    .show()
            }
            is AppStateMovies.Loading -> { loadingLayout.visibility = View.VISIBLE }
        }
    }

    private fun initRecyclerView(recyclerView: RecyclerView, data: List<Categories>) {
        recyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        adapter.setGenresData(data)
        adapter.setOnMovieAdapter(object : GenresAdapter.OnSetMovieAdapter {
            override fun setMovieAdapter(itemView: View, moviesData: Categories) {
                val movieDataRecycler: RecyclerView = itemView.findViewById(R.id.recycler_movieData)
                val movieDataLayoutManager =
                    LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
                val movieDataAdapter = MovieAdapter()

                movieDataRecycler.layoutManager = movieDataLayoutManager
                movieDataRecycler.adapter = movieDataAdapter
                movieDataAdapter.setMovieData(moviesData.movieList)

                movieDataAdapter.setOnItemClickListener(object : MovieAdapter.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                        sharedPref.pagePreferences = PAGE_DETAILS
                        val bundle = Bundle()
                        bundle.putParcelable(MovieDetails.KEY, moviesData.movieList[position])
                        activity?.apply {
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.main, MovieDetails.newInstance(bundle, viewModel))
                                .addToBackStack(null)
                                .commit()
                        }
                    }
                })
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.average -> {
                if (item.isChecked) {
                    viewModel.getMoviesData(averageBasic)
                    sharedPref.setSharedPref(IS_CHECKED, false)
                } else {
                    viewModel.getMoviesData(averageMin)
                    sharedPref.setSharedPref(IS_CHECKED, true)
                }
                item.isChecked = !item.isChecked
            }
            R.id.menu_favorite -> {
                sharedPref.pagePreferences = PAGE_FAVORITE
                activity?.apply {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main, FavoriteFragment.newInstance())
                        .addToBackStack(null)
                        .commit()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}