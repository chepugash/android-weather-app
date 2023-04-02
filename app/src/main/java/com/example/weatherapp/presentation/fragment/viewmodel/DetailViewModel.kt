package com.example.weatherapp.presentation.fragment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.di.DataContainer.weatherByIdUseCase
import com.example.weatherapp.domain.entity.WeatherInfo
import com.example.weatherapp.domain.usecase.GetWeatherByIdUseCase
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {

    private val getWeatherByIdUseCase = weatherByIdUseCase

    private val _weatherInfo = MutableLiveData<WeatherInfo?>(null)
    val weatherInfo: LiveData<WeatherInfo?>
        get() = _weatherInfo

    private val _error = MutableLiveData<Throwable?>(null)
    val error: LiveData<Throwable?>
        get() = _error

    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean>
        get() = _loading

    fun getWeather(id: Int) {
        viewModelScope.launch {
            try {
                _loading.value = true
                getWeatherByIdUseCase(id).also { weatherInfo ->
                    _weatherInfo.value = weatherInfo
                }
            } catch (error: Throwable) {
                _error.value = error
            } finally {
                _loading.value = false
            }
        }
    }
}