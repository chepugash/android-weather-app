package com.example.weatherapp.data.core.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class LangInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val newURL = original.url.newBuilder()
            .addQueryParameter("lang", "ru")
            .build()

        return chain.proceed(
            original.newBuilder()
                .url(newURL)
                .build()
        )
    }

}