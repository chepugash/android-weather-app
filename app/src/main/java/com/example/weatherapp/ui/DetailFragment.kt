package com.example.weatherapp.ui

import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
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

private const val NE_START = 23
private const val E_START = 68
private const val SE_START = 113
private const val S_START = 158
private const val WS_START = 203
private const val W_START = 248
private const val NW_START = 293
private const val N_START = 337
private const val N_END = 360

private const val PRESSURE_FORMULA = 1.333

class DetailFragment : Fragment(R.layout.fragment_detail) {

    private var binding: FragmentDetailBinding? = null

    private val api = DataContainer.weatherApi

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailBinding.bind(view)
        (activity as AppCompatActivity?)?.supportActionBar?.title = ""

        val id = arguments?.getInt(ARG_NAME)

        binding?.run {
            if (id != null) {
                loadWeather(id)
            }
        }
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
                    showPressure(it.main.pressure)
                    it.weather.firstOrNull()?.also {
                        showWeatherIcon(it.icon)
                    }
                    (activity as AppCompatActivity?)?.supportActionBar?.title = it.name
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

    private fun showPressure(pressure: Int) {
        binding?.run {
            tvPressure.text = "${castMbarToMm(pressure)} мм.рт.ст"
            ivPressure.isVisible = true
        }
    }

    private fun castMbarToMm(pressure: Int): Int = (pressure / PRESSURE_FORMULA).toInt()

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
    }

    private fun convertWind(wind: Int): String {
        return when (wind) {
            in 0..NE_START -> "С"
            in NE_START..E_START -> "СВ"
            in E_START..SE_START -> "В"
            in SE_START..S_START -> "ЮВ"
            in S_START..WS_START -> "Ю"
            in WS_START..W_START -> "ЮЗ"
            in W_START..NW_START -> "З"
            in NW_START..N_START -> "СЗ"
            in N_START..N_END -> "С"
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