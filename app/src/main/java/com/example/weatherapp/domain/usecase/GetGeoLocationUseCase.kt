package com.example.weatherapp.domain.usecase

import com.example.weatherapp.domain.entity.GeoLocationInfo
import com.example.weatherapp.domain.repository.GeoLocationRepository
import com.google.android.gms.location.FusedLocationProviderClient

class GetGeoLocationUseCase(
    private val geoLocationRepository: GeoLocationRepository
) {

    suspend operator fun invoke(
        client: FusedLocationProviderClient
    ): GeoLocationInfo = geoLocationRepository.getGeoLocation(client)
}