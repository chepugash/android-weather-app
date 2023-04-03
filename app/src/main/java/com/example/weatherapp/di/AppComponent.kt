package com.example.weatherapp.di

import android.content.Context
import com.example.weatherapp.presentation.fragment.DetailFragment
import com.example.weatherapp.presentation.fragment.MainFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class, NetworkModule::class, WeatherModule::class, GeoModule::class])
@Singleton
interface AppComponent {

    fun provideContext(): Context

    fun inject(weatherFragment: MainFragment)

    fun inject(detailFragment: DetailFragment)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(applicationContext: Context): Builder

        fun build(): AppComponent
    }
}