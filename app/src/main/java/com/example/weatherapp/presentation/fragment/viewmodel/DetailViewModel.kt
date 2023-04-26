package com.example.weatherapp.presentation.fragment.viewmodel

import androidx.lifecycle.*
import com.example.weatherapp.domain.entity.WeatherInfo
import com.example.weatherapp.domain.usecase.GetWeatherByIdUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class DetailViewModel @AssistedInject constructor(
    private val weatherByIdUseCase: GetWeatherByIdUseCase,
    @Assisted private val cityId: Int
) : ViewModel() {

    private val _weatherInfo = MutableLiveData<WeatherInfo?>(null)
    val weatherInfo: LiveData<WeatherInfo?>
        get() = _weatherInfo

    private val _error = MutableLiveData<Throwable?>(null)
    val error: LiveData<Throwable?>
        get() = _error

    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean>
        get() = _loading

    fun getWeather() {
        viewModelScope.launch {
            try {
                _loading.value = true
                weatherByIdUseCase(cityId).also { weatherInfo ->
                    _weatherInfo.value = weatherInfo
                }
            } catch (error: Throwable) {
                _error.value = error
            } finally {
                _loading.value = false
            }
        }
    }

    @AssistedFactory
    interface DetailViewModelFactory {
        fun create(cityId: Int): DetailViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: DetailViewModelFactory,
            cityId: Int
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T: ViewModel> create(modelClass: Class<T>): T =
                assistedFactory.create(cityId) as T
        }
    }
}