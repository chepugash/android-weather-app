package com.example.weatherapp.adapter

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.weatherapp.data.response.City
import com.example.weatherapp.databinding.ItemCityBinding

class CityItem(
    private val binding: ItemCityBinding,
    private val action: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(city: City) {
        with(binding) {
            tvName.text = city.name
            tvTemp.text = city.main.temp.toString()
            ivIcon.load("https://openweathermap.org/img/w/${city.weather.firstOrNull()?.icon}.png") {
                crossfade(true)
            }
            root.setOnClickListener {
                action(city.id)
            }
        }
    }
}