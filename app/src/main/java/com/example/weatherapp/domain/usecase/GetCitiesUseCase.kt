package com.example.weatherapp.domain.usecase

import com.example.weatherapp.domain.entity.CityInfo
import com.example.weatherapp.domain.repository.WeatherRepository

class GetCitiesUseCase(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(
        lat: Double,
        lon: Double,
        cnt: Int
    ): List<CityInfo> = weatherRepository.getCities(lat, lon, cnt)
}