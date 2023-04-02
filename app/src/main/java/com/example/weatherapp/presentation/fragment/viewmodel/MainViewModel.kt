package com.example.weatherapp.presentation.fragment.viewmodel

import android.annotation.SuppressLint
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.di.DataContainer.citiesUseCase
import com.example.weatherapp.di.DataContainer.geoLocationUseCase
import com.example.weatherapp.di.DataContainer.weatherByNameUseCase
import com.example.weatherapp.domain.entity.CityInfo
import com.example.weatherapp.domain.entity.WeatherInfo
import com.example.weatherapp.utils.SingleLiveEvent
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val getWeatherByNameUseCase = weatherByNameUseCase

    private val getCitiesUseCase = citiesUseCase

    private val getGeoLocationUseCase = geoLocationUseCase

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
                getWeatherByNameUseCase(name).also { weatherInfo ->
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
                getCitiesUseCase(lat, lon, count). also { list ->
                    _cityList.value = list
                }
            } catch (error: Throwable) {
                _error.value = error
            } finally {
                _loading.value = false
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun loadNearestCities(mFusedLocationClient: FusedLocationProviderClient ?= null) {
        if (mFusedLocationClient == null) {
            getCities()
        } else {
            mFusedLocationClient.lastLocation.addOnSuccessListener { loc: Location? ->
                if (loc != null) {
                    getCities(loc.latitude, loc.longitude)
                } else {
                    getCities()
                }
            }
        }
    }

    private companion object {
        private const val DEFAULT_LAT = 49.0
        private const val DEFAULT_LON = 11.5
        private const val CNT = 10
    }

}
