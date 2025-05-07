package domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class CurrentWeather(
    val time: String,
    val interval: String,
    @SerialName("temperature_2m") val temperature2m: Double,
    @SerialName("relative_humidity_2m") val relativeHumidity2m: Double,
    @SerialName("apparent_temperature") val apparentTemperature: Double,
    @SerialName("is_day") val isDay: Int,
    val precipitation: Double,
    val rain: Double,
    val showers: Double,
    val snowfall: Double,
    @SerialName("weather_code") val weatherCode: Int,
    @SerialName("cloud_cover") val cloudCover: Int,
    @SerialName("wind_speed_10m") val windSpeed10m: Double,
    @SerialName("wind_direction_10m") val windDirection10m: Int,
    @SerialName("wind_gusts_10m") val windGusts10m: Double
)