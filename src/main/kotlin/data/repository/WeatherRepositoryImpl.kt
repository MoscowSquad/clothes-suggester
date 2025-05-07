package data.repository

import WeatherRepository
import domain.models.CurrentWeather
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class WeatherRepositoryImpl(private val httpClient: HttpClient) : WeatherRepository {
    override suspend fun getCurrentWeather(latitude: Double, longitude: Double): CurrentWeather {
        val response: HttpResponse = httpClient.get("https://api.open-meteo.com/v1/forecast") {
            parameter("latitude", latitude)
            parameter("longitude", longitude)
            parameter("current", "temperature_2m,relative_humidity_2m,apparent_temperature,is_day,wind_speed_10m,snowfall,rain,weather_code,cloud_cover")
        }

        if (!response.status.isSuccess()) {
            throw Exception("Failed to fetch weather data: ${response.status}")
        }

        return response.body()
    }
}
