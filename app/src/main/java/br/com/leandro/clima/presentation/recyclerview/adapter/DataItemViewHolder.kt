package br.com.leandro.clima.presentation.recyclerview.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import br.com.leandro.clima.data.Forecast
import br.com.leandro.clima.databinding.ItemForecastCardBinding
import br.com.leandro.clima.databinding.ItemHeaderBinding
import br.com.leandro.clima.presentation.util.Helper.Companion.convertTime
import br.com.leandro.clima.presentation.util.Helper.Companion.timeIsBetween
import br.com.leandro.clima.presentation.util.Helper.Companion.weatherImage

sealed class DataItemViewHolder(open val binding: ViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    class ForecastCardViewHolder(override val binding: ItemForecastCardBinding) :
        DataItemViewHolder(binding) {

        @SuppressLint("SetTextI18n")
        fun bind(item: Forecast) {
            with(binding) {
                binding.hour.text = convertTime(item.dt)
                binding.description.text = item.weather.first().description
                binding.tempText.text = item.main.temp.toInt().toString()
                binding.feelsLike.text = "Sensação: ${item.main.feels_like.toInt()}ºC"
                binding.minTempText.text = "Mín: ${item.main.temp_min.toInt()}ºC"
                binding.maxTempText.text = "Max: ${item.main.temp_max.toInt()}ºC"
                val duringTheDay = timeIsBetween("06:00", "18:00", convertTime(item.dt))
                weatherImage(item.weather.first().description, duringTheDay, binding.image)
                executePendingBindings()
            }


        }

        companion object {
            fun from(parent: ViewGroup): ForecastCardViewHolder {
                val binding: ItemForecastCardBinding = ItemForecastCardBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ForecastCardViewHolder(binding)
            }
        }
    }

    class HeaderViewHolder(override val binding: ItemHeaderBinding) :
        DataItemViewHolder(binding) {

        fun bind(item: DataItem.Header) {
            with(binding) {
                binding.itemHeaderText.text = item.header
                executePendingBindings()
            }
        }

        companion object {
            fun from(parent: ViewGroup): HeaderViewHolder {
                val binding: ItemHeaderBinding = ItemHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return HeaderViewHolder(binding)
            }
        }
    }
}
