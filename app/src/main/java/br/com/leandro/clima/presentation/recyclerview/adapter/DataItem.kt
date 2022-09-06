package br.com.leandro.clima.presentation.recyclerview.adapter

import br.com.leandro.clima.data.Forecast

sealed class DataItem {

    abstract val id: Long

    data class ForecastCardItem(
        val forecast: Forecast,
        override val id: Long = forecast.id
    ) : DataItem()

    data class Header(
        val header: String,
        override val id: Long = Long.MIN_VALUE
    ) : DataItem()
}
