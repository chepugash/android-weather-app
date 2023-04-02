package com.example.weatherapp.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentDetailBinding
import com.example.weatherapp.presentation.fragment.viewmodel.DetailViewModel
import com.example.weatherapp.utils.showSnackbar
import timber.log.Timber

class DetailFragment : Fragment(R.layout.fragment_detail) {

    private var binding: FragmentDetailBinding? = null
    private val viewModel: DetailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailBinding.bind(view)
        observeViewModel()
        (activity as AppCompatActivity?)?.supportActionBar?.title = ""

        val id = arguments?.getInt(ARG_NAME)

        binding?.run {
            if (id != null) {
                viewModel.getWeather(id)
            }
        }
    }

    private fun observeViewModel() {
        with(viewModel) {
            loading.observe(viewLifecycleOwner) {
                binding?.progress?.isVisible = it
            }
            weatherInfo.observe(viewLifecycleOwner) {
                if (it == null) return@observe
                showCityName(it.city)
                showTemp(it.temperature)
                showHumidity(it.humidity)
                showWind(it.windSpeed, it.windDir)
                showPressure(it.pressure)
                showWeatherIcon(it.icon)
            }
            error.observe(viewLifecycleOwner) {
                if (it == null) return@observe
                showError(it)
            }
        }
    }

    private fun showError(error: Throwable) {
        activity?.findViewById<View>(android.R.id.content)
            ?.showSnackbar(error.message ?: "Error")
    }

    private fun showWeatherIcon(id: String) {
        Timber.e(id)
        binding?.ivIcon?.load("https://openweathermap.org/img/w/$id.png") {
            crossfade(true)
        }
    }

    private fun showPressure(pressure: Int) {
        binding?.run {
            tvPressure.text = "$pressure мм.рт.ст"
            ivPressure.isVisible = true
        }
    }

    private fun showTemp(temp: Double) {
        binding?.run {
            tvTemp.text = "$temp°C"
            ivTemp.isVisible = true
        }
    }

    private fun showHumidity(humidity: Int) {
        binding?.run {
            tvHumidity.text = "$humidity%"
            ivHumidity.isVisible = true
        }
    }

    private fun showWind(speed: Double, direction: String) {
        binding?.run {
            tvWind.text = "$speed м/c, $direction"
            ivWind.isVisible = true
        }
    }

    private fun showCityName(name: String) {
        binding?.tvCity?.run {
            text = name
        }
        (activity as AppCompatActivity?)?.supportActionBar?.title = name
    }

    companion object {
        private const val ARG_NAME = "id"

        fun newInstance(id: Int) = DetailFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_NAME, id)
            }
        }
    }
}