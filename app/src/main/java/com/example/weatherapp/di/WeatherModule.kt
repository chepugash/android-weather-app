package com.example.weatherapp.di

import com.example.weatherapp.data.weather.WeatherRepositoryImpl
import com.example.weatherapp.data.weather.datasource.remote.WeatherApi
import com.example.weatherapp.domain.repository.WeatherRepository
import com.example.weatherapp.domain.usecase.GetCitiesUseCase
import com.example.weatherapp.domain.usecase.GetWeatherByIdUseCase
import com.example.weatherapp.domain.usecase.GetWeatherByNameUseCase
import dagger.Module
import dagger.Provides

@Module
class WeatherModule {

    @Provides
    fun provideWeatherRepository(
        weatherApi: WeatherApi
    ): WeatherRepository = WeatherRepositoryImpl(weatherApi)

    @Provides
    fun provideWeatherByNameUseCase(
        repository: WeatherRepository
    ): GetWeatherByNameUseCase = GetWeatherByNameUseCase(repository)

    @Provides
    fun provideWeatherByIdUseCase(
        repository: WeatherRepository
    ): GetWeatherByIdUseCase = GetWeatherByIdUseCase(repository)

    @Provides
    fun provideCitiesUseCase(
        repository: WeatherRepository
    ): GetCitiesUseCase = GetCitiesUseCase(repository)
}
