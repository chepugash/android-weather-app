package com.example.weatherapp.data.weather.mapper

import com.example.weatherapp.data.weather.datasource.remote.response.City
import com.example.weatherapp.domain.entity.CityInfo

private fun City.toCityInfo(): CityInfo = CityInfo(
    id = id,
    temperature = main.temp,
    name = name,
    icon = weather.first().icon
)

fun List<City>.toCityInfoList(): List<CityInfo> {
    val cityInfoList = arrayListOf<CityInfo>()
    for (city in this) {
        cityInfoList.add(city.toCityInfo())
    }
    return cityInfoList
}