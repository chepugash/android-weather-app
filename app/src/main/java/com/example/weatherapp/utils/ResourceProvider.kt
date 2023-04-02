package com.example.weatherapp.utils

interface ResourceProvider {

    fun getString(id: Int): String

    fun getColor(id: Int): Int
}