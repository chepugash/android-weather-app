package com.example.weatherapp.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.R
import com.example.weatherapp.adapter.CityAdapter
import com.example.weatherapp.adapter.SpaceItemDecorator
import com.example.weatherapp.data.DataContainer
import com.example.weatherapp.data.response.City
import com.example.weatherapp.databinding.FragmentMainBinding
import com.example.weatherapp.utils.hideKeyboard
import com.example.weatherapp.utils.showSnackbar
import com.google.android.gms.common.api.Api.ApiOptions.HasOptions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch

private const val DEFAULT_LAT = 49.0
private const val DEFAULT_LON = 11.5
private const val CNT = "10"

class MainFragment : Fragment(R.layout.fragment_main) {

    private var binding: FragmentMainBinding? = null
    private var adapter: CityAdapter? = null
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    private val api = DataContainer.weatherApi

    private var userLat = DEFAULT_LAT
    private var userLon = DEFAULT_LON

    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            loadNearestCities()
            activity?.findViewById<View>(android.R.id.content)
                ?.showSnackbar("Granted")
        } else {
            createRecyclerView()
            activity?.findViewById<View>(android.R.id.content)
                ?.showSnackbar("Not granted")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)

        val menuItem = menu.findItem(R.id.action_search)
        val searchView = menuItem.actionView as androidx.appcompat.widget.SearchView

        searchView.queryHint = "Найти город"

        searchView.setOnQueryTextListener(
            object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    loadCity(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean = false

        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val actionBar = (activity as AppCompatActivity?)?.supportActionBar
        actionBar?.title = "Найти город"
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)
        loadNearestCities()
    }

    private fun loadNearestCities() {
        if (context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } == PackageManager.PERMISSION_DENIED) {
            requestPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            mFusedLocationClient = LocationServices
                .getFusedLocationProviderClient(requireActivity())
            mFusedLocationClient.lastLocation.addOnSuccessListener { loc: Location? ->
                if (loc != null) {
                    userLat = loc.latitude
                    userLon = loc.longitude
                    activity?.findViewById<View>(android.R.id.content)
                        ?.showSnackbar("Cities founded")
                    createRecyclerView()
                } else {
                    activity?.findViewById<View>(android.R.id.content)
                        ?.showSnackbar("Not found")
                    createRecyclerView()
                }
            }
        }
    }

    private fun createRecyclerView() {
        val itemDecoration = SpaceItemDecorator(
            this@MainFragment,
            16.0f
        )
        lifecycleScope.launch {
            try {
                showLoading(true)
                adapter = CityAdapter(
                    api.getCities(userLat, userLon, CNT).list
                ) {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.container, DetailFragment.newInstance(it))
                        .addToBackStack("Main")
                        .commit()
                }
                binding?.rvCities?.run {
                    adapter = this@MainFragment.adapter
                    this.addItemDecoration(itemDecoration)
                }
            } catch (error: Throwable) {
                showError(error)
            } finally {
                showLoading(false)
            }
        }
    }

    private fun loadCity(query: String) {
        lifecycleScope.launch {
            try {
                showLoading(true)
                api.getWeather(query).also {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.container, DetailFragment.newInstance(it.id))
                        .addToBackStack("Main")
                        .commit()
                }
            } catch (error: Throwable) {
                showError(error)
            } finally {
                showLoading(false)
            }
        }
    }

    private fun showLoading(isShow: Boolean) {
        binding?.progress?.isVisible = isShow
    }

    private fun showError(error: Throwable) {
        activity?.findViewById<View>(android.R.id.content)
            ?.showSnackbar(error.message ?: "Error")
    }
}
