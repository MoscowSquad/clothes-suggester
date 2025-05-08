package data.repository

import WeatherRepository
import domain.models.CurrentWeather
import domain.models.exceptions.FailedFetchWeatherDataException
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

const val OPEN_METO_API = "https://api.open-meteo.com/v1/forecast"

class WeatherRepositoryImpl(private val httpClient: HttpClient) : WeatherRepository {
    override suspend fun getCurrentWeather(latitude: Double, longitude: Double): CurrentWeather {
        val response: HttpResponse = httpClient.get(OPEN_METO_API) {
            parameter("latitude", latitude)
            parameter("longitude", longitude)
            parameter(
                "current",
                "temperature_2m,relative_humidity_2m,apparent_temperature,is_day,wind_speed_10m,snowfall,rain,weather_code,cloud_cover"
            )
        }

        if (!response.status.isSuccess()) {
            throw FailedFetchWeatherDataException(
                "Weather API returned error status: ${response.status}"
            )
        }

        return response.body()
    }
}
