package br.com.leandro.clima.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.leandro.clima.dao.CityDao
import br.com.leandro.clima.data.CurrentWeather
import br.com.leandro.clima.data.WeatherForecast
import br.com.leandro.clima.repository.CurrentWeatherRepository
import br.com.leandro.clima.repository.WeatherForecastRepository

class MainViewModel(
    private val cityDao: CityDao
) : ViewModel() {

    companion object {
        const val TAG = "MainViewModel"
    }

    init {
        update()
    }

    fun update() {
        getCurrentWeather()
        getWeatherForecast()
    }

    private var _currentWeatherList = MutableLiveData<CurrentWeather?>().apply { value = null }
    val currentWeatherList: LiveData<CurrentWeather?>
        get() = _currentWeatherList

    private var _weatherForecastList = MutableLiveData<WeatherForecast?>().apply { value = null }
    val weatherForecastList: LiveData<WeatherForecast?>
        get() = _weatherForecastList

    private fun getCurrentWeather() {
        Thread {
            try {
                _currentWeatherList.postValue(
                    CurrentWeatherRepository(cityDao)
                        .getCurrentWeather()
                )
            } catch (exception: Exception) {
                Log.e(TAG, exception.message.toString())
            }
        }.start()
    }

    private fun getWeatherForecast() {
        Thread {
            try {
                _weatherForecastList.postValue(
                    WeatherForecastRepository(cityDao)
                        .getWeatherForecast()
                )
            } catch (exception: Exception) {
                Log.e(TAG, exception.message.toString())
            }
        }.start()
    }
}

