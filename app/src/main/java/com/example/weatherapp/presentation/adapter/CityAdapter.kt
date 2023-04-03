package com.example.weatherapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.ItemCityBinding
import com.example.weatherapp.domain.entity.CityInfo

class CityAdapter(
    private val action: (Int) -> Unit
) : ListAdapter<CityInfo, RecyclerView.ViewHolder>(object: DiffUtil.ItemCallback<CityInfo>() {
    override fun areItemsTheSame(
        oldItem: CityInfo,
        newItem: CityInfo
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: CityInfo,
        newItem: CityInfo
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
        (holder as CityItem).onBind(currentList[position])
    }

    override fun getItemCount(): Int = currentList.size
}
