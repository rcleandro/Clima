package br.com.leandro.clima.data

import androidx.room.PrimaryKey
import java.io.Serializable

data class WeatherForecast(
    val cod: String,
    val message: Int,
    val cnt: Int,
    val list: List<Forecast>,
    val city: ForecastCity,
)

data class Forecast(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val dt: Long,
    val main: MainForecast,
    val weather: ArrayList<ForecastWeather>,
    val clouds: CloudsForecast,
    val wind: WindForecast,
    val visibility: Int,
    val pop: Double,
    val rain: RainForecast?,
    val sys: SysForecast,
    val dt_txt: String
)

data class ForecastCity(
    val id: Long,
    val name: String,
    val coord: CoordForecast,
    val country: String,
    val population: Long,
    val timezone: Int,
    val sunrise: Long,
    val sunset: Long
)

class MainForecast(
    var temp: Float,
    var feels_like: Float,
    var temp_min: Float,
    var temp_max: Float,
    var pressure: Float,
    var sea_level: Int,
    var grnd_level: Int,
    var humidity: Float,
    var temp_kf: Float?
) : Serializable

class ForecastWeather(
    var id: Int,
    var main: String,
    var description: String,
    var icon: String
) : Serializable

class CloudsForecast(
    var all: Float
) : Serializable

class RainForecast(
    var h3: Float
) : Serializable

class WindForecast(
    var speed: Float,
    var gust: Float,
    var deg: Float
) : Serializable

class SysForecast(
    var country: String?,
    var sunrise: Long?,
    var sunset: Long?,
    var pod: String?
) : Serializable

class CoordForecast(
    var lon: Float,
    var lat: Float
) : Serializable

