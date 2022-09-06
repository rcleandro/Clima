package br.com.leandro.clima.presentation.recyclerview.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.leandro.clima.R
import br.com.leandro.clima.data.Details
import br.com.leandro.clima.data.DetailsType
import br.com.leandro.clima.databinding.ItemMainDetailsBinding

class WeatherDetailsAdapter(
    private var detailsList: List<Details>
) :
    RecyclerView.Adapter<WeatherDetailsAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            ItemMainDetailsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = detailsList.count()

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(detailsList[position])
    }

    class Holder(
        private val binding: ItemMainDetailsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var detail: Details

        @SuppressLint("SetTextI18n")
        fun bind(detail: Details) {
            this.detail = detail

            when (detail.tipe) {
                DetailsType.WIND -> {
                    binding.image.setImageResource(R.drawable.ic_wind_speed)
                    binding.title.text = detail.title
                    binding.text.text = detail.text
                }
                DetailsType.VISIBILITY -> {
                    binding.image.setImageResource(R.drawable.ic_visibility)
                    binding.title.text = detail.title
                    binding.text.text = detail.text
                }
                DetailsType.HUMIDITY -> {
                    binding.image.setImageResource(R.drawable.ic_humidity)
                    binding.title.text = detail.title
                    binding.text.text = detail.text
                }
                DetailsType.PRESSURE -> {
                    binding.image.setImageResource(R.drawable.ic_atmospheric_pressure)
                    binding.title.text = detail.title
                    binding.text.text = detail.text
                }
                DetailsType.ALTITUDE -> {
                    binding.image.setImageResource(R.drawable.ic_altitude)
                    binding.title.text = detail.title
                    binding.text.text = detail.text
                }
                DetailsType.SUNRISE -> {
                    binding.image.setImageResource(R.drawable.ic_sunrise)
                    binding.title.text = detail.title
                    binding.text.text = detail.text
                }
                DetailsType.SUNSET -> {
                    binding.image.setImageResource(R.drawable.ic_sunset)
                    binding.title.text = detail.title
                    binding.text.text = detail.text
                }
            }
        }
    }
}