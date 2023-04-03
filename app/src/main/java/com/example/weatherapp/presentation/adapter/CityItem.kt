package com.example.weatherapp.presentation.adapter

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ItemCityBinding
import com.example.weatherapp.domain.entity.CityInfo

class CityItem(
    private val binding: ItemCityBinding,
    private val action: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(city: CityInfo) {
        with(binding) {
            tvName.text = city.name
            tvTemp.text = city.temperature.toString()
            tvTemp.setTextColor(
                ContextCompat.getColor(tvTemp.context, getColor(city.temperature))
            )
            ivIcon.load("https://openweathermap.org/img/w/${city.icon}.png") {
                crossfade(true)
            }
            root.setOnClickListener {
                action(city.id)
            }
        }
    }

    private fun getColor(temp: Double): Int {
        return when (temp) {
            in TEMP_M_20..TEMP_M_15 -> R.color.blue_900
            in TEMP_M_15..TEMP_M_10 -> R.color.blue_800
            in TEMP_M_10..TEMP_M_5 -> R.color.blue_700
            in TEMP_M_5..TEMP_0 -> R.color.blue_600
            in TEMP_0..TEMP_5 -> R.color.amber_600
            in TEMP_5..TEMP_10 -> R.color.amber_700
            in TEMP_10..TEMP_15 -> R.color.amber_800
            in TEMP_15..TEMP_20 -> R.color.amber_900
            else -> R.color.grey_700
        }
    }

    companion object {
        private const val TEMP_M_20 = -20.0
        private const val TEMP_M_15 = -15.0
        private const val TEMP_M_10 = -10.0
        private const val TEMP_M_5 = -5.0
        private const val TEMP_0 = 0.0
        private const val TEMP_5 = 5.0
        private const val TEMP_10 = 10.0
        private const val TEMP_15 = 15.0
        private const val TEMP_20 = 20.0
    }
}