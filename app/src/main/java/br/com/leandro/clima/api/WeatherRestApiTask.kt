package br.com.leandro.clima.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherRestApiTask {
    companion object {
        private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

        private fun weatherProvider(): Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        fun retrofitApi(): WeatherApi = weatherProvider().create(WeatherApi::class.java)
    }
}