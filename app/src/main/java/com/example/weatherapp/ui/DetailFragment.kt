package com.example.weatherapp.ui

import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.weatherapp.R
import com.example.weatherapp.data.DataContainer
import com.example.weatherapp.databinding.FragmentDetailBinding
import com.example.weatherapp.utils.showSnackbar
import kotlinx.coroutines.launch
import timber.log.Timber

class DetailFragment : Fragment(R.layout.fragment_detail) {

    private var binding: FragmentDetailBinding? = null

    private val api = DataContainer.weatherApi

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailBinding.bind(view)

        val id = arguments?.getInt(ARG_NAME)

        binding?.run {
            if (id != null) {
                loadWeather(id)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun loadWeather(query: Int) {
        lifecycleScope.launch {
            try {
                showLoading(true)
                api.getWeather(query).also {
                    showCityName(it.name)
                    showTemp(it.main.temp)
                    showHumidity(it.main.humidity)
                    showWind(it.wind.speed, convertWind(it.wind.deg))
                    it.weather.firstOrNull()?.also {
                        showWeatherIcon(it.icon)
                    }
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

    private fun showWeatherIcon(id: String) {
        Timber.e(id)
        binding?.ivIcon?.load("https://openweathermap.org/img/w/$id.png") {
            crossfade(true)
        }
    }

    private fun showTemp(temp: Double) {
        binding?.run {
            tvTemp.text = "$temp C"
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
    }

    private fun convertWind(wind: Int): String {
        return when (wind) {
            in 0..22 -> "С"
            in 23..67 -> "СВ"
            in 68..112 -> "В"
            in 113..157 -> "ЮВ"
            in 158..202 -> "Ю"
            in 203..247 -> "ЮЗ"
            in 248..292 -> "З"
            in 293..337 -> "СЗ"
            in 337..360 -> "С"
            else -> "Не определено"
        }
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

// название
// температура
// пикча
// влажность
// направление ветра