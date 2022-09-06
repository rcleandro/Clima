package br.com.leandro.clima.repository

import android.util.Log
import br.com.leandro.clima.api.WeatherRestApiTask
import br.com.leandro.clima.dao.CityDao
import br.com.leandro.clima.data.CurrentWeather
import br.com.leandro.clima.data.UserSessionManager

class CurrentWeatherRepository(
    cityDao: CityDao
) {

    companion object {
        const val TAG = "CurrentRepository"
    }

    private val cityLocation = cityDao.getAll()
    private val userLocation = UserSessionManager.location.value

    fun getCurrentWeather(): CurrentWeather? {
        val latitude =
            if (cityLocation.isNotEmpty()) cityLocation.first().latitude
            else userLocation?.latitude

        val longitude =
            if (cityLocation.isNotEmpty()) cityLocation.first().longitude
            else userLocation?.longitude

        val request = WeatherRestApiTask.retrofitApi()
            .getCurrentWeather(latitude, longitude)?.execute()
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



