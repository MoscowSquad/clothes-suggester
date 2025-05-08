package domain.use_cases

import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import domain.models.CurrentWeather
import domain.repository.WeatherRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetCurrentWeatherUseCaseTest {

    private lateinit var weatherRepository: WeatherRepository
    private lateinit var getCurrentWeatherUseCase: GetCurrentWeatherUseCase

    private val latitude = 55.7558
    private val longitude = 37.6173
    private val expectedWeather = CurrentWeather(
        temperature2m = 12.5,
        relativeHumidity2m = 67.0,
        apparentTemperature = 11.2,
        isDay = 1,
        rain = 0.1,
        snowfall = 0.0,
        cloudCover = 65.0,
        windSpeed10m = 6.8,
    )

    @BeforeEach
    fun setup() {
        weatherRepository = mockk(relaxed = true)
        getCurrentWeatherUseCase = GetCurrentWeatherUseCase(weatherRepository)
    }

    @Test
    fun `should get the current weather from the WeatherRepository when getting the current weather`() =
        runTest {
            // Given
            coEvery { weatherRepository.getCurrentWeather(latitude, longitude) } returns expectedWeather

            // When
            getCurrentWeatherUseCase.getCurrentWeather(latitude, longitude)

            // Then
            coVerify(exactly = 1) { weatherRepository.getCurrentWeather(latitude, longitude) }
        }

    @Test
    fun `should return current weather for given coordinates when getting the current weather`() = runTest {
        // Given
        coEvery { weatherRepository.getCurrentWeather(latitude, longitude) } returns expectedWeather

        // When
        val result = getCurrentWeatherUseCase.getCurrentWeather(latitude, longitude)

        // Then
        Truth.assertThat(expectedWeather).isEqualTo(result)
    }

    @Test

    fun `fun getCurrentWeather() should throw exception when repository fails`() = runTest {

        // Given
        val exceptionMessage = "API error"
        coEvery { weatherRepository.getCurrentWeather(latitude, longitude) } throws RuntimeException(exceptionMessage)

        // When / Then
        val exception = assertThrows<RuntimeException> {
            getCurrentWeatherUseCase.getCurrentWeather(latitude, longitude)
        }

        // Assert
        assertThat(exception.message).isEqualTo(exceptionMessage)
    }

    @Test
    fun `getCurrentWeather() should return expected weather data when valid latitude and longitude are provided()`() =
        runTest {
            // Given
            val testLat = 33.3
            val testLon = 44.4
            val expected = expectedWeather.copy(temperature2m = 25.0)
            coEvery { weatherRepository.getCurrentWeather(testLat, testLon) } returns expected

            // When
            val result = getCurrentWeatherUseCase.getCurrentWeather(testLat, testLon)

            // Then
            coVerify { weatherRepository.getCurrentWeather(testLat, testLon) }
            assertThat(result).isEqualTo(expected)
        }


    @Test
    fun `getCurrentWeather() should call repository with zero coordinates when latitude and longitude are both zeros`() =
        runTest {
            val zeroLat = 0.0
            val zeroLon = 0.0
            coEvery { weatherRepository.getCurrentWeather(zeroLat, zeroLon) } returns expectedWeather

            // When
            getCurrentWeatherUseCase.getCurrentWeather(zeroLat, zeroLon)

            // Then
            coVerify { weatherRepository.getCurrentWeather(zeroLat, zeroLon) }
        }
}