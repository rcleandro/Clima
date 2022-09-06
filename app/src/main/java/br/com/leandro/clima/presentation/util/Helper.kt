package br.com.leandro.clima.presentation.util

import android.app.Activity
import android.os.Build
import android.widget.ImageView
import br.com.leandro.clima.R
import br.com.leandro.clima.data.CurrentWeather
import br.com.leandro.clima.data.Details
import br.com.leandro.clima.data.DetailsType
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

class Helper {
    companion object {
        fun weatherImage(description: String, duringTheDay: Boolean, image: ImageView) {
            when {
                duringTheDay && description == "céu limpo" ->
                    image.setImageResource(R.drawable.ic_clear_sunny)
                !duringTheDay && description == "céu limpo" ->
                    image.setImageResource(R.drawable.ic_clear_moon)
                description == "nublado" ->
                    image.setImageResource(R.drawable.ic_cloudy)
                description == "chuva leve" ->
                    image.setImageResource(R.drawable.ic_medium_rain)
                description == "chuva moderada" ->
                    image.setImageResource(R.drawable.ic_heavy_rain)
                description == "garoa de leve intensidade" ->
                    image.setImageResource(R.drawable.ic_light_drizzle)
                description == "chuviscos com intensidade de raios" ->
                    image.setImageResource(R.drawable.ic_lightning)
                duringTheDay && description == "nuvens dispersas" ->
                    image.setImageResource(R.drawable.ic_sun_and_cloudy)
                !duringTheDay && description == "nuvens dispersas" ->
                    image.setImageResource(R.drawable.ic_moon_and_cloudy)
                duringTheDay && description == "algumas nuvens" ->
                    image.setImageResource(R.drawable.ic_sun_and_cloudy)
                !duringTheDay && description == "algumas nuvens" ->
                    image.setImageResource(R.drawable.ic_moon_and_cloudy)
            }
        }

        fun timeIsBetween(startTime: String, endTime: String, comparatorHour: String?): Boolean {
            val start = startTime.replace(":", "").toInt()
            val end = endTime.replace(":", "").toInt()
            val time =
                if (comparatorHour != null) comparatorHour.replace(":", "").toInt()
                else {
                    val date = Date()
                    val now = Calendar.getInstance()
                    now.time = date
                    now[Calendar.HOUR_OF_DAY] * 100 + now[Calendar.MINUTE]
                }

            return end > start && time >= start && time <= end
                    || end < start && (time >= start || time <= end)
        }

        fun convertTime(time: Long): String {
            return SimpleDateFormat("HH:mm", Locale.getDefault())
                .format(Date(time * 1000))
        }

        fun convertDate(time: Long): String {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val zoneOffset = ZoneOffset.ofHours(-3)
                val instant = Instant.ofEpochSecond(time)
                val formatter = DateTimeFormatter
                    .ofPattern("EEE, d MMMM", Locale.getDefault())
                return instant.atOffset(zoneOffset).format(formatter)
            }
            return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                .format(Date(time * 1000))
        }

        fun listDetailsMock(activity: Activity, weather: CurrentWeather): List<Details> {
            return listOf(
                Details(
                    DetailsType.WIND,
                    activity.getString(R.string.wind_speed),
                    "${weather.wind.speed} m/seg"
                ),
                Details(
                    DetailsType.VISIBILITY,
                    activity.getString(R.string.visibility),
                    weather.visibility.toString()
                ),
                Details(
                    DetailsType.HUMIDITY,
                    activity.getString(R.string.humidity),
                    "${weather.main.humidity.toInt()}%"
                ),
                Details(
                    DetailsType.PRESSURE,
                    activity.getString(R.string.atmospheric_pressure),
                    "${weather.main.pressure.toInt()} hPa"
                ),
                Details(
                    DetailsType.ALTITUDE,
                    activity.getString(R.string.altitude),
                    "${weather.main.grnd_level} metros"
                ),
                Details(
                    DetailsType.SUNRISE,
                    activity.getString(R.string.sunrise),
                    convertTime(weather.sys.sunrise ?: 0)
                ),
                Details(
                    DetailsType.SUNSET,
                    activity.getString(R.string.sunset),
                    convertTime(weather.sys.sunset ?: 0)
                )
            )
        }
    }
}