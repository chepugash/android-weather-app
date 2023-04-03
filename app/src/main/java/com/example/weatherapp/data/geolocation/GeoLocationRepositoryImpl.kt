package com.example.weatherapp.data.geolocation

import com.example.weatherapp.domain.entity.GeoLocationInfo
import com.example.weatherapp.domain.repository.GeoLocationRepository

class GeoLocationRepositoryImpl(
    private val geoLocationDataSource: GeoLocationDataSource?
) : GeoLocationRepository {

    override suspend fun getGeoLocation(): GeoLocationInfo? = geoLocationDataSource?.getLastLocation()
}