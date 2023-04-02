package com.example.weatherapp.data.weather.mapper

import com.example.weatherapp.data.weather.datasource.remote.response.WeatherResponse
import com.example.weatherapp.domain.entity.WeatherInfo

private const val NE_START = 23
private const val E_START = 68
private const val SE_START = 113
private const val S_START = 158
private const val WS_START = 203
private const val W_START = 248
private const val NW_START = 293
private const val N_START = 337
private const val N_END = 360
private const val PRESSURE_FORMULA = 1.333

fun WeatherResponse.toWeatherInfo(): WeatherInfo = WeatherInfo(
    id = id,
    city = name,
    temperature = main.temp,
    icon = weather.first().icon,
    humidity = main.humidity,
    windSpeed = wind.speed,
    windDeg = wind.deg,
    windDir = when(wind.deg) {
        in 0..NE_START -> "С"
        in NE_START..E_START -> "СВ"
        in E_START..SE_START -> "В"
        in SE_START..S_START -> "ЮВ"
        in S_START..WS_START -> "Ю"
        in WS_START..W_START -> "ЮЗ"
        in W_START..NW_START -> "З"
        in NW_START..N_START -> "СЗ"
        in N_START..N_END -> "С"
        else -> "Не определено"
    },
    pressure = (main.pressure / PRESSURE_FORMULA).toInt()
)

fun List<WeatherResponse>.toWeathers(): List<WeatherInfo> = map {
    it.toWeatherInfo()
}