package com.example.themovie.ui.main

import android.app.AlertDialog
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.themovie.R
import com.example.themovie.databinding.MainFragmentBinding
import com.example.themovie.model.AppStateMovies
import com.example.themovie.model.Categories
import com.example.themovie.util.NIGHT_MODE
import com.example.themovie.util.SharedPref
import com.example.themovie.viewModel.MainViewModel
import com.google.android.material.snackbar.Snackbar

class MainFragment : Fragment() {
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!
    private val connection = Connect { Toast.makeText(context, "$it", Toast.LENGTH_LONG).show() }
    private lateinit var sharedPref: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = SharedPref(requireActivity())
        if (savedInstanceState == null) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        else if (!sharedPref.getSharedPref(NIGHT_MODE))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                requireActivity().onBackPressed()
                return true
            }
            R.id.app_bar_settings -> {
                val list = arrayOf("Светлая тема", "Темная тема")
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Тема приложения").setItems(list) { _, which ->
                    when (which) {
                        0 -> {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                            sharedPref.setSharedPref(NIGHT_MODE, false)
                        }
                        1 -> {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                            sharedPref.setSharedPref(NIGHT_MODE, true)
                        }
                    }
                    ActivityCompat.recreate(requireActivity())
                }
                builder.create().apply {
                    window?.setBackgroundDrawableResource(R.drawable.dialog_shape)
                    show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun makeToast(status: Boolean) =
        Toast.makeText(context, "$status", Toast.LENGTH_LONG).show()

    companion object {
        fun newInstance() = MainFragment()
        private var isMain = true
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

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAppBar()
        viewModel.getLiveData().observe(viewLifecycleOwner, { renderData(it) })
    }

    @RequiresApi(Build.VERSION_CODES.N)
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
                    .setAction("Reload") { viewModel.getMoviesData() }
                    .show()
            }
            is AppStateMovies.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }
        }
    }

    private fun initRecyclerView(recyclerView: RecyclerView, data: List<Categories>) {
        recyclerView.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val adapter = GenresAdapter(requireActivity().supportFragmentManager)

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        adapter.setGenresData(data)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_navigation, menu)
    }

    private fun setAppBar() {
        val context = activity as MainActivity
        context.setSupportActionBar(context.findViewById(R.id.toolbar))
        setHasOptionsMenu(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}