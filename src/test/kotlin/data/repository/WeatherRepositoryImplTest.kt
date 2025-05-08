package data.repository

import com.google.common.truth.Truth
import domain.models.CurrentWeather
import domain.models.FailedFetchWeatherDataException
import domain.models.NoLocationRetrieved
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class WeatherRepositoryImplTest {
    private lateinit var httpClient: HttpClient
    private lateinit var weatherRepository: WeatherRepositoryImpl

    private fun setUp(handler: MockRequestHandleScope.(HttpRequestData) -> HttpResponseData) {
        val mockEngine = MockEngine(handler)
        httpClient = HttpClient(mockEngine)
        weatherRepository = WeatherRepositoryImpl(httpClient)
    }

    @Test
    fun `getLocation() should return current weather when getting the current weather from the api`() = runTest {
        // Given
        val latitude = 29.9791854
        val longitude = 31.1316879
        setUp { _ ->
            respond(
                content = getFakeResponse(),
                status = HttpStatusCode.OK, headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        // When
        val result = weatherRepository.getCurrentWeather(latitude, longitude)

        // Then
        Truth.assertThat(result).isEqualTo(getFakeWeather())

    }

    @Test
    fun `getLocation() should ignore other JSON keys when getting current weather from the api`() = runTest {
        // Given
        val latitude = 29.9791854
        val longitude = 31.1316879
        setUp { _ ->
            respond(
                content = getFakeResponse(),
                status = HttpStatusCode.OK, headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        // When
        val result = weatherRepository.getCurrentWeather(latitude, longitude)

        // Then
        Truth.assertThat(result).isEqualTo(getFakeWeather())
    }

    @Test
    fun `getLocation() should throw FailedFetchWeatherDataException when error happen after request the api`() =
        runTest {
            // Given
            val latitude = 29.9791854
            val longitude = 31.1316879
            setUp { _ ->
                respond(
                    content = getFakeResponse(),
                    status = HttpStatusCode.NotFound, headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }

            // When, Then
            org.junit.jupiter.api.assertThrows<FailedFetchWeatherDataException> {
                weatherRepository.getCurrentWeather(latitude, longitude)
            }
        }

    @Test
    fun `getLocation() should throw NoLocationRetrieved when response not correct`() = runTest {
        // Given
        val latitude = 29.9791854
        val longitude = 31.1316879
        setUp { _ ->
            respond(
                content = """<html itemscope="" itemtype="http://schema.org/WebPage" lang="en">""",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        // When, Then
        org.junit.jupiter.api.assertThrows<NoLocationRetrieved> {
            weatherRepository.getCurrentWeather(latitude, longitude)
        }
    }

    private fun getFakeResponse(): String {
        return """
        {
            "latitude": 52.52,
            "longitude": 13.419998,
            "generationtime_ms": 0.1004934310913086,
            "utc_offset_seconds": 0,
            "timezone": "GMT",
            "timezone_abbreviation": "GMT",
            "elevation": 38,
            "current_units": {
                "time": "iso8601",
                "interval": "seconds",
                "temperature_2m": "°C",
                "relative_humidity_2m": "%",
                "apparent_temperature": "°C",
                "is_day": "",
                "rain": "mm",
                "showers": "mm",
                "snowfall": "cm",
                "wind_speed_10m": "km/h",
                "cloud_cover": "%"
            },
            "current": {
                "time": "2025-05-08T15:30",
                "interval": 900,
                "temperature_2m": 14.9,
                "relative_humidity_2m": 31,
                "apparent_temperature": 11.8,
                "is_day": 1,
                "rain": 0,
                "showers": 0,
                "snowfall": 0,
                "wind_speed_10m": 5.2,
                "cloud_cover": 93
            }
        }
    """.trimIndent()
    }

    fun getFakeWeather(): CurrentWeather {
        return CurrentWeather(
            temperature2m = 14.9,
            relativeHumidity2m = 31.0,
            apparentTemperature = 11.8,
            isDay = 1,
            rain = 0.0,
            snowfall = 0.0,
            windSpeed10m = 5.2,
            cloudCover = 93.0,
        )
    }
}