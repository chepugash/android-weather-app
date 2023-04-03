package com.example.weatherapp.presentation.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.weatherapp.App
import com.example.weatherapp.R
import com.example.weatherapp.presentation.adapter.CityAdapter
import com.example.weatherapp.presentation.adapter.SpaceItemDecorator
import com.example.weatherapp.databinding.FragmentMainBinding
import com.example.weatherapp.domain.usecase.GetCitiesUseCase
import com.example.weatherapp.domain.usecase.GetGeoLocationUseCase
import com.example.weatherapp.domain.usecase.GetWeatherByNameUseCase
import com.example.weatherapp.presentation.fragment.viewmodel.MainViewModel
import com.example.weatherapp.utils.showSnackbar
import javax.inject.Inject

class MainFragment : Fragment(R.layout.fragment_main) {

    private var binding: FragmentMainBinding? = null

    @Inject
    lateinit var getCitiesUseCase: GetCitiesUseCase

    @Inject
    lateinit var getWeatherByNameUseCase: GetWeatherByNameUseCase

    @Inject
    lateinit var getGeoLocationUseCase: GetGeoLocationUseCase

    private val viewModel: MainViewModel by viewModels {
        MainViewModel.provideFactory(
            getGeoLocationUseCase,
            getWeatherByNameUseCase,
            getCitiesUseCase
        )
    }

    private val adapter: CityAdapter by lazy(LazyThreadSafetyMode.NONE) {
        CityAdapter {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, DetailFragment.newInstance(it))
                .addToBackStack("Main")
                .commit()
        }
    }

    private val itemDecoration by lazy(LazyThreadSafetyMode.NONE) {
        SpaceItemDecorator(
            this@MainFragment,
            16.0f
        )
    }

    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            showSnackbar("Granted")
            viewModel.loadNearestCities(isGranted = true)
        } else {
            showSnackbar("Not granted")
            viewModel.loadNearestCities(isGranted = false)
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
                        viewModel.getWeather(query)
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean = false
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent.inject(this)
        super.onCreate(savedInstanceState)
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
        binding?.rvCities?.addItemDecoration(itemDecoration)
        observeViewModel()
        loadNearestCities()
    }

    private fun observeViewModel() {
        with(viewModel) {
            loading.observe(viewLifecycleOwner) {
                binding?.progress?.isVisible = it
            }
            weatherInfo.observe(viewLifecycleOwner) {
                if (it == null) return@observe
            }
            cityList.observe(viewLifecycleOwner) { list ->
                if (list == null) return@observe
                adapter.submitList(list)
                binding?.rvCities?.run {
                    adapter = this@MainFragment.adapter
                }
            }
            error.observe(viewLifecycleOwner) {
                if (it == null) return@observe
                showError(it)
            }
            navigateToDetails.observe(viewLifecycleOwner) {
                if (it == null) return@observe
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, DetailFragment.newInstance(it))
                    .addToBackStack("Main")
                    .commit()
            }
        }
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
            viewModel.loadNearestCities(isGranted = true)
        }
    }

    private fun showSnackbar(message: String) {
        activity?.findViewById<View>(android.R.id.content)
            ?.showSnackbar(message)
    }

    private fun showError(error: Throwable) {
        activity?.findViewById<View>(android.R.id.content)
            ?.showSnackbar(error.message ?: "Error")
    }
}
