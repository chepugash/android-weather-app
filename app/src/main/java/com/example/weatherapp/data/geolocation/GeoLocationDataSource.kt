package com.example.weatherapp.data.geolocation

import android.annotation.SuppressLint
import com.example.weatherapp.domain.entity.GeoLocationInfo
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class GeoLocationDataSource(
    val client: FusedLocationProviderClient
) {

    @SuppressLint("MissingPermission")
    suspend fun getLastLocation(): GeoLocationInfo = client.getLastLocation().await().let {
       Timber.e(it.toString())
        GeoLocationInfo(
            lon = it.longitude,
            lat = it.latitude
        )
    }
}