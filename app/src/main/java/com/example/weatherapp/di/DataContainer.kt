package com.example.weatherapp.di

import android.content.Context
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.weather.datasource.remote.WeatherApi
import com.example.weatherapp.data.core.interceptor.ApiKeyInterceptor
import com.example.weatherapp.data.core.interceptor.LangInterceptor
import com.example.weatherapp.data.core.interceptor.UnitsInterceptor
import com.example.weatherapp.data.geolocation.GeoLocationDataSource
import com.example.weatherapp.data.geolocation.GeoLocationRepositoryImpl
import com.example.weatherapp.data.weather.WeatherRepositoryImpl
import com.example.weatherapp.domain.usecase.GetCitiesUseCase
import com.example.weatherapp.domain.usecase.GetGeoLocationUseCase
import com.example.weatherapp.domain.usecase.GetWeatherByIdUseCase
import com.example.weatherapp.domain.usecase.GetWeatherByNameUseCase
import com.example.weatherapp.utils.AndroidResourceProvider
import com.example.weatherapp.utils.ResourceProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object DataContainer {

    private const val BASE_URL = BuildConfig.API_ENDPOINT

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    private val httpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(ApiKeyInterceptor())
            .addInterceptor(LangInterceptor())
            .addInterceptor(UnitsInterceptor())
            .connectTimeout(10L, TimeUnit.SECONDS)
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
    }

    private val weatherApi = retrofit.create(WeatherApi::class.java)

    private val weatherRepository = WeatherRepositoryImpl(weatherApi)

    val weatherByNameUseCase: GetWeatherByNameUseCase
        get() = GetWeatherByNameUseCase(weatherRepository)

    val weatherByIdUseCase: GetWeatherByIdUseCase
        get() = GetWeatherByIdUseCase(weatherRepository)

    val citiesUseCase: GetCitiesUseCase
        get() = GetCitiesUseCase(weatherRepository)

    val geoLocationUseCase: GetGeoLocationUseCase
        get() = GetGeoLocationUseCase(geoLocationRepository)

    private val geoLocationRepository = GeoLocationRepositoryImpl(geoLocationDataSource)

    private var locationClient: FusedLocationProviderClient? = null

    fun provideGeoLocationProviderClient(
        applicationContext: Context
    ) {
        locationClient = LocationServices.getFusedLocationProviderClient(applicationContext)
    }

    @Suppress("TooGenericExceptionThrown")
    private val geoLocationDataSource: GeoLocationDataSource
        get() = GeoLocationDataSource(
            locationClient ?: throw Exception("locationClient not provided")
        )
}
