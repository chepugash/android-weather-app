package com.example.weatherapp.domain.repository

import com.example.weatherapp.domain.entity.GeoLocationInfo

interface GeoLocationRepository {

    suspend fun getGeoLocation(): GeoLocationInfo?
}