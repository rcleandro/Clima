package br.com.leandro.clima.repository

import android.util.Log
import br.com.leandro.clima.api.WeatherRestApiTask
import br.com.leandro.clima.dao.CityDao
import br.com.leandro.clima.data.UserSessionManager
import br.com.leandro.clima.data.WeatherForecast

class WeatherForecastRepository(
    cityDao: CityDao
) {

    companion object {
        const val TAG = "ForecastRepository"
    }

    private val cityLocation = cityDao.getAll()
    private val userLocation = UserSessionManager.location.value

    fun getWeatherForecast(): WeatherForecast? {
        val latitude =
            if (cityLocation.isNotEmpty()) cityLocation.first().latitude
            else userLocation?.latitude

        val longitude =
            if (cityLocation.isNotEmpty()) cityLocation.first().longitude
            else userLocation?.longitude

        val request = WeatherRestApiTask.retrofitApi()
            .getWeatherForecast(latitude, longitude)?.execute()
        if (request != null) {
            if (request.isSuccessful) {
                request.body()?.let {
                    return it
                }
            } else {
                request.errorBody()?.let {
                    Log.d(TAG, it.toString())
                }
            }
        }
        return null
    }
}



