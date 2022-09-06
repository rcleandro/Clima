package br.com.leandro.clima.data

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object UserSessionManager {
    private val _location = MutableLiveData<Location>(null)
    fun updateLocation(occurrence: Location) = _location.postValue(occurrence)
    val location: LiveData<Location>
        get() = _location

    private val _city = MutableLiveData<City>(null)
    fun updateCity(city: City) = _city.postValue(city)
    val city: LiveData<City>
        get() = _city
}