package com.example.weatherapp

import android.app.Application
import com.example.weatherapp.di.DataContainer.provideGeoLocationProviderClient
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        provideGeoLocationProviderClient(this)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}