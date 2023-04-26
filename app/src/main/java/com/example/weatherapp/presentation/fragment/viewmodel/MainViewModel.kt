package com.example.weatherapp.presentation.fragment.viewmodel

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.weatherapp.domain.entity.CityInfo
import com.example.weatherapp.domain.entity.WeatherInfo
import com.example.weatherapp.domain.usecase.GetCitiesUseCase
import com.example.weatherapp.domain.usecase.GetGeoLocationUseCase
import com.example.weatherapp.domain.usecase.GetWeatherByNameUseCase
import com.example.weatherapp.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val geoLocationUseCase: GetGeoLocationUseCase,
    private val weatherByNameUseCase: GetWeatherByNameUseCase,
    private val citiesUseCase: GetCitiesUseCase
) : ViewModel() {

    private val _weatherInfo = SingleLiveEvent<WeatherInfo?>()
    val weatherInfo: SingleLiveEvent<WeatherInfo?>
        get() = _weatherInfo

    private val _cityList = MutableLiveData<List<CityInfo>>(null)
    val cityList: LiveData<List<CityInfo>>
        get() = _cityList

    private val _error = MutableLiveData<Throwable?>(null)
    val error: LiveData<Throwable?>
        get() = _error

    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean>
        get() = _loading

    private val _navigateToDetails = SingleLiveEvent<Int?>()
    val navigateToDetails: SingleLiveEvent<Int?>
        get() = _navigateToDetails


    fun getWeather(name: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                weatherByNameUseCase(name).also { weatherInfo ->
                    _weatherInfo.value = weatherInfo
                    _navigateToDetails.value = weatherInfo.id
                }
            } catch (error: Throwable) {
                _error.value = error
            } finally {
                _loading.value = false
            }
        }
    }

    private fun getCities(lat: Double = DEFAULT_LAT, lon: Double = DEFAULT_LON, count: Int = CNT) {
        viewModelScope.launch {
            try {
                _loading.value = true
                citiesUseCase(lat, lon, count). also { list ->
                    _cityList.value = list
                }
            } catch (error: Throwable) {
                _error.value = error
            } finally {
                _loading.value = false
            }
        }
    }

    fun loadNearestCities(isGranted: Boolean) {
        viewModelScope.launch {
            try {
                if (isGranted) {
                    val location = geoLocationUseCase()
                    if (location == null) {
                        getCities()
                    } else {
                        getCities(location.lat, location.lon)
                    }
                }
                else {
                    getCities()
                }
            } catch (error: Exception) {
                _error.value = error
            }
        }
    }

    companion object {
        private const val DEFAULT_LAT = 49.0
        private const val DEFAULT_LON = 11.5
        private const val CNT = 10

        fun provideFactory(
            geoLocationUseCase: GetGeoLocationUseCase,
            weatherByNameUseCase: GetWeatherByNameUseCase,
            citiesUseCase: GetCitiesUseCase
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                // Create a SavedStateHandle for this ViewModel from extras
//                val savedStateHandle = extras.createSavedStateHandle()
                MainViewModel(geoLocationUseCase, weatherByNameUseCase, citiesUseCase)
            }
        }
    }

}
