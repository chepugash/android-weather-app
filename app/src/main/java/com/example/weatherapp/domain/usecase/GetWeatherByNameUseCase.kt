package com.example.weatherapp.domain.usecase

import com.example.weatherapp.domain.entity.WeatherInfo
import com.example.weatherapp.domain.repository.WeatherRepository

class GetWeatherByNameUseCase(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(
        query: String
    ): WeatherInfo = weatherRepository.getWeather(query)
}