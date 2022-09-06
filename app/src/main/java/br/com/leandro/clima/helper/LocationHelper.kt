package br.com.leandro.clima.helper

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Looper
import android.util.Log
import br.com.leandro.clima.data.UserSessionManager
import com.google.android.gms.location.*
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY

class LocationHelper(
    private var activity: Activity,
    private var callback: Callback
) {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    fun getLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)
        setupLocationTracking()
    }

    @SuppressLint("MissingPermission")
    private fun setupLocationTracking() {
        val request = LocationRequest.create().apply {
            interval = 200 * 1000
            fastestInterval = 120 * 1000
            priority = PRIORITY_HIGH_ACCURACY
        }

        fusedLocationProviderClient.requestLocationUpdates(
            request,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
            p0.locations.let { locations ->
                for (location in locations) {
                    Log.d(
                        "LocationHelper",
                        "Obtained Location -> Lat: ${location.latitude} | Lng: ${location.longitude} "
                    )
                    UserSessionManager.updateLocation(location)
                    callback.onLocationAcquired()
                }
            }
            fusedLocationProviderClient.removeLocationUpdates(this)
        }
    }

    interface Callback {
        fun onLocationAcquired()
    }
}