package data.repository

import data.util.parseResponse
import domain.models.CurrentWeather
import domain.models.FailedFetchWeatherDataException
import domain.models.NoLocationRetrieved
import domain.repository.WeatherRepository
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.json.JSONObject

const val OPEN_METO_API = "https://api.open-meteo.com/v1/forecast"

class WeatherRepositoryImpl(private val httpClient: HttpClient) : WeatherRepository {
    override suspend fun getCurrentWeather(latitude: Double, longitude: Double): CurrentWeather {
        val httpResponse: HttpResponse = httpClient.get(OPEN_METO_API) {
            parameter("latitude", latitude)
            parameter("longitude", longitude)
            parameter(
                "current",
                "temperature_2m,relative_humidity_2m,apparent_temperature,is_day,rain,showers,snowfall,wind_speed_10m,cloud_cover"
            )
        }

        if (httpResponse.status != HttpStatusCode.OK) {
            throw FailedFetchWeatherDataException(httpResponse.status)
        }

        try {
            val response = JSONObject(httpResponse.bodyAsText()).get("current").toString()
            return response.parseResponse<CurrentWeather>()
        } catch (e: Exception) {
            throw NoLocationRetrieved()
        }
    }
}
