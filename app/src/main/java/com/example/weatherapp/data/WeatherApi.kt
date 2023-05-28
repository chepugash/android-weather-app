package com.example.weatherapp.data

import com.example.weatherapp.data.response.CityResponse
import com.example.weatherapp.data.response.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("weather")
    suspend fun getWeather(
        @Query("q") city: String
    ): WeatherResponse

    @GET("weather")
    suspend fun getWeather(
        @Query("id") city: Int
    ): WeatherResponse

    @GET("find")
    suspend fun getCities(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("cnt") cnt: String
    ): CityResponse
}