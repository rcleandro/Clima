package br.com.leandro.clima.api

import br.com.leandro.clima.data.CurrentWeather
import br.com.leandro.clima.data.WeatherForecast
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("weather?appid=76a35a17f3e1bce771a09f3555b614a8&units=metric&lang=pt_br")
    fun getCurrentWeather(
        @Query("lat") latitude: Double?,
        @Query("lon") longitude: Double?
    ): Call<CurrentWeather?>?

    @GET("forecast?appid=76a35a17f3e1bce771a09f3555b614a8&units=metric&lang=pt_br")
    fun getWeatherForecast(
        @Query("lat") latitude: Double?,
        @Query("lon") longitude: Double?
    ): Call<WeatherForecast?>?
}