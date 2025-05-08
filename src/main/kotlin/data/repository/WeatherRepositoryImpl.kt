package data.repository

import WeatherRepository
import domain.models.CurrentWeather
import domain.models.NoLocationRetrieved
import domain.models.exceptions.FailedFetchWeatherDataException
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import org.json.JSONObject

const val OPEN_METO_API = "https://api.open-meteo.com/v1/forecast"

class WeatherRepositoryImpl(private val httpClient: HttpClient) : WeatherRepository {
    override suspend fun getCurrentWeather(latitude: Double, longitude: Double): CurrentWeather {
        val httpResponse: HttpResponse = httpClient.get(OPEN_METO_API) {
            parameter("latitude", latitude)
            parameter("longitude", longitude)
            parameter(
                "current",
                "temperature_2m,relative_humidity_2m,apparent_temperature,is_day,wind_speed_10m,snowfall,rain,weather_code,cloud_cover"
            )
        }

        if (httpResponse.status != HttpStatusCode.OK) {
            throw FailedFetchWeatherDataException(
                "Weather API returned error status: ${httpResponse.status}"
            )
        }

        try {
            val response = JSONObject(httpResponse.bodyAsText()).get("current").toString()
            val json = Json {
                ignoreUnknownKeys = true
            }
            return json.decodeFromString<CurrentWeather>(response)
        } catch (e: Exception) {
            throw NoLocationRetrieved()
        }
    }
}
