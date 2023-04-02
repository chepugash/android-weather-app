package com.example.weatherapp.domain.entity

data class WeatherInfo(
    val id: Int,
    val city: String,
    val temperature: Double,
    val icon: String,
    val humidity: Int,
    val windSpeed: Double,
    val windDeg: Int,
    val windDir: String,
    val pressure: Int
)
