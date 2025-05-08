package domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeather(
    @SerialName("temperature_2m") val temperature2m: Double,
    @SerialName("relative_humidity_2m") val relativeHumidity2m: Double,
    @SerialName("apparent_temperature") val apparentTemperature: Double,
    @SerialName("is_day") val isDay: Int,
    val rain: Double,
    val snowfall: Double,
    @SerialName("cloud_cover") val cloudCover: Double,
    @SerialName("wind_speed_10m") val windSpeed10m: Double,
)