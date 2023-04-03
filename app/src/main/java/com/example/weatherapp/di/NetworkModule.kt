package com.example.weatherapp.di

import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.core.interceptor.ApiKeyInterceptor
import com.example.weatherapp.data.core.interceptor.LangInterceptor
import com.example.weatherapp.data.core.interceptor.UnitsInterceptor
import com.example.weatherapp.data.weather.datasource.remote.WeatherApi
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named

@Module
class NetworkModule {

    @Provides
    @Named("logger")
    fun provideLoggingInterceptor(): Interceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    @Provides
    @Named("api_key")
    fun provideApiKeyInterceptor(): Interceptor = ApiKeyInterceptor()

    @Provides
    @Named("lang")
    fun provideLangInterceptor(): Interceptor = LangInterceptor()

    @Provides
    @Named("units")
    fun provideUnitsInterceptor(): Interceptor = UnitsInterceptor()

    @Provides
    fun provideHttpClient(
        @Named("logger") loggingInterceptor: Interceptor,
        @Named("api_key") apiKeyInterceptor: Interceptor,
        @Named("lang") langInterceptor: Interceptor,
        @Named("units") unitsInterceptor: Interceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(apiKeyInterceptor)
        .addInterceptor(langInterceptor)
        .addInterceptor(unitsInterceptor)
        .connectTimeout(10L, TimeUnit.SECONDS)
        .build()

    @Provides
    fun provideRetrofit(
        httpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
        @Named("base_url") baseUrl: String
    ): Retrofit = Retrofit.Builder()
        .client(httpClient)
        .addConverterFactory(gsonConverterFactory)
        .baseUrl(baseUrl)
        .build()

    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    fun provideWeatherApi(
        retrofit: Retrofit
    ): WeatherApi = retrofit.create(WeatherApi::class.java)

    @Provides
    @Named("base_url")
    fun provideBaseUrl(): String = BuildConfig.API_ENDPOINT
}
