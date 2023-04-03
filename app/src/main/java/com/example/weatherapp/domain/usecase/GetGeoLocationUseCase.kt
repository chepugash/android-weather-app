package com.example.weatherapp.domain.usecase

import com.example.weatherapp.domain.entity.GeoLocationInfo
import com.example.weatherapp.domain.repository.GeoLocationRepository

class GetGeoLocationUseCase(
    private val geoLocationRepository: GeoLocationRepository
) {

    suspend operator fun invoke(): GeoLocationInfo? = geoLocationRepository.getGeoLocation()
}