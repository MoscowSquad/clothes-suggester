package logic.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeather(
    val time: String,
    val interval: String,
    @SerialName("temperature_2m") val temperature2m: String,
    @SerialName("relative_humidity_2m") val relativeHumidity2m: String,
    @SerialName("apparent_temperature") val apparentTemperature: String,
    @SerialName("is_day") val isDay: String,
    val precipitation: String,
    val rain: String,
    val showers: String,
    val snowfall: String,
    @SerialName("weather_code") val weatherCode: String,
    @SerialName("cloud_cover") val cloudCover: String,
    @SerialName("wind_speed_10m") val windSpeed10m: String,
    @SerialName("wind_direction_10m") val windDirection10m: String,
    @SerialName("wind_gusts_10m") val windGusts10m: String
)