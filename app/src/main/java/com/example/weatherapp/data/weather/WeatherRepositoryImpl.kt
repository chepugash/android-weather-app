package com.example.weatherapp.data.weather

import com.example.weatherapp.data.weather.datasource.remote.WeatherApi
import com.example.weatherapp.data.weather.mapper.toCityInfoList
import com.example.weatherapp.data.weather.mapper.toWeatherInfo
import com.example.weatherapp.domain.entity.CityInfo
import com.example.weatherapp.domain.entity.WeatherInfo
import com.example.weatherapp.domain.repository.WeatherRepository

class WeatherRepositoryImpl(
    private val api: WeatherApi
) : WeatherRepository {

    override suspend fun getWeather(
        query: String
    ): WeatherInfo = api.getWeather(query).toWeatherInfo()

    override suspend fun getWeather(
        query: Int
    ): WeatherInfo = api.getWeather(query).toWeatherInfo()

    override suspend fun getCities(
        lat: Double,
        lon: Double,
        cnt: Int
    ): List<CityInfo> = api.getCities(lat, lon, cnt.toString()).list.toCityInfoList()
}