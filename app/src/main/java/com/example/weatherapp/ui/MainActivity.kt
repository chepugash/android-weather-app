package com.example.weatherapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import coil.load

import com.example.weatherapp.data.DataContainer
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.utils.hideKeyboard
import com.example.weatherapp.utils.showSnackbar
import kotlinx.coroutines.launch
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    private val api = DataContainer.weatherApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        binding?.run {
            btnLoad.setOnClickListener {
                onLoadClick()
            }
            etCity.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    onLoadClick()
                }
                true
            }
        }
    }

    private fun onLoadClick() {
        binding?.run {
            etCity.hideKeyboard()
            loadWeather(etCity.text.toString())
        }
    }

    private fun loadWeather(query: String) {
        lifecycleScope.launch {
            try {
                showLoading(true)
                api.getWeather(query).also {
                    showTemp(it.main.temp)
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
        findViewById<View>(android.R.id.content)
            .showSnackbar(error.message ?: "Error")
    }

    private fun showTemp(temp: Double) {
        binding?.tvTemp?.run {
            text = "Temp: $temp C"
            isVisible = true
        }
    }

    private fun showWeatherIcon(id: String) {
        Timber.e(id)
        binding?.ivIcon?.load("https://openweathermap.org/img/w/$id.png") {
            crossfade(true)
        }
    }

}
