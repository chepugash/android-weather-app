package com.example.weatherapp.domain.repository

import com.example.weatherapp.domain.entity.GeoLocationInfo
import com.google.android.gms.location.FusedLocationProviderClient

interface GeoLocationRepository {

    suspend fun getGeoLocation(): GeoLocationInfo?
}