package domain.use_cases

import GetCurrentWeatherUseCase
import WeatherRepository
import domain.models.CurrentWeather
 import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import com.google.common.truth.Truth.assertThat

@OptIn(ExperimentalCoroutinesApi::class)
class GetCurrentWeatherUseCaseTest {

    private lateinit var weatherRepository: WeatherRepository
    private lateinit var getCurrentWeatherUseCase: GetCurrentWeatherUseCase

    private val latitude = 55.7558
    private val longitude = 37.6173
    private val expectedWeather = CurrentWeather(
        time = "2025-05-06T15:00",
        interval = "900",
        temperature2m = 12.5,
        relativeHumidity2m = 67.0,
        apparentTemperature = 11.2,
        isDay = 1,
        precipitation = 0.1,
        rain = 0.1,
        showers = 0.0,
        snowfall = 0.0,
        weatherCode = 3,
        cloudCover = 65,
        windSpeed10m = 6.8,
        windDirection10m = 80,
        windGusts10m = 14.2
    )

    @BeforeEach
    fun setup() {
        weatherRepository = mockk(relaxed = true)
        getCurrentWeatherUseCase = GetCurrentWeatherUseCase(weatherRepository)
    }

    @Test
    fun `should return current weather for given coordinates when getting the current weather`() = runTest {
        // Given
        coEvery { weatherRepository.getCurrentWeather(latitude, longitude) } returns expectedWeather

        // When
        val result = getCurrentWeatherUseCase(latitude, longitude)

        // Then
        coVerify(exactly = 1) { weatherRepository.getCurrentWeather(latitude, longitude) }
        assertEquals(expectedWeather, result)
    }

    @Test
    fun `should throw exception when repository fails`() = runTest {
        // Given
        val exceptionMessage = "API error"
        coEvery { weatherRepository.getCurrentWeather(latitude, longitude) } throws RuntimeException(exceptionMessage)

        try {
            // When
            getCurrentWeatherUseCase(latitude, longitude)
            fail("Exception was expected")
        } catch (e: RuntimeException) {
            // Then
            assertThat(e.message).isEqualTo(exceptionMessage)
        }
    }

    @Test
    fun `should return weather with valid temperature value`() = runTest {
        // Given
        val modifiedWeather = expectedWeather.copy(temperature2m = 20.8)
        coEvery { weatherRepository.getCurrentWeather(latitude, longitude) } returns modifiedWeather

        // When
        val result = getCurrentWeatherUseCase(latitude, longitude)

        // Then
        assertThat(result.temperature2m).isEqualTo(20.8)
    }

    @Test
    fun `should call repository with zero coordinates`() = runTest {
        // Given
        val zeroLat = 0.0
        val zeroLon = 0.0
        coEvery { weatherRepository.getCurrentWeather(zeroLat, zeroLon) } returns expectedWeather

        // When
        val result = getCurrentWeatherUseCase(zeroLat, zeroLon)

        // Then
        coVerify { weatherRepository.getCurrentWeather(zeroLat, zeroLon) }
    }
}