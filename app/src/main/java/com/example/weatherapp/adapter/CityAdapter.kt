package com.example.weatherapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.data.response.City
import com.example.weatherapp.databinding.ItemCityBinding

class CityAdapter(
    private val list: List<City>,
    private val action: (Int) -> Unit
) : ListAdapter<City, RecyclerView.ViewHolder>(object: DiffUtil.ItemCallback<City>() {
    override fun areItemsTheSame(
        oldItem: City,
        newItem: City
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: City,
        newItem: City
    ): Boolean = oldItem == newItem
}) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CityItem = CityItem(
        binding = ItemCityBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ),
        action = action
    )

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        (holder as CityItem).onBind(list[position])
    }

    override fun getItemCount(): Int = list.size
}
