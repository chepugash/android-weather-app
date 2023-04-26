package com.example.weatherapp.di

import com.example.weatherapp.data.geolocation.GeoLocationDataSource
import com.example.weatherapp.data.geolocation.GeoLocationRepositoryImpl
import com.example.weatherapp.domain.repository.GeoLocationRepository
import com.example.weatherapp.domain.usecase.GetGeoLocationUseCase
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class GeoModule {

    @Provides
    fun provideGeoLocationUseCase(
        repository: GeoLocationRepository
    ): GetGeoLocationUseCase = GetGeoLocationUseCase(repository)

    @Provides
    fun provideGeoLocationRepository(
        dataSource: GeoLocationDataSource,
    ): GeoLocationRepository = GeoLocationRepositoryImpl(dataSource)

    @Provides
    fun provideGeoLocationDataSource(
        client: FusedLocationProviderClient
    ): GeoLocationDataSource = GeoLocationDataSource(client)

}