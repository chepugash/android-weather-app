package com.example.weatherapp.domain.repository

import com.example.weatherapp.domain.entity.CityInfo
import com.example.weatherapp.domain.entity.WeatherInfo

interface WeatherRepository {

    suspend fun getWeather(query: String): WeatherInfo

    suspend fun getWeather(query: Int): WeatherInfo

    suspend fun getCities(lat: Double, lon: Double, cnt: Int): List<CityInfo>
}