package com.example.weatherapp.presentation.fragment.viewmodel

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.weatherapp.domain.entity.WeatherInfo
import com.example.weatherapp.domain.usecase.GetWeatherByIdUseCase
import kotlinx.coroutines.launch

class DetailViewModel(
    weatherByIdUseCase: GetWeatherByIdUseCase
) : ViewModel() {

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

    companion object {
        fun provideFactory(
            weatherByIdUseCase: GetWeatherByIdUseCase,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                // Create a SavedStateHandle for this ViewModel from extras
//                val savedStateHandle = extras.createSavedStateHandle()
                DetailViewModel(weatherByIdUseCase)
            }
        }
    }
}