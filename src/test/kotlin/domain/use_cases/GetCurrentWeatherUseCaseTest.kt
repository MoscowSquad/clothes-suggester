package domain.use_cases

import domain.models.CurrentWeather
import domain.repository.WeatherRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


@OptIn(ExperimentalCoroutinesApi::class)
 class GetCurrentWeatherUseCaseTest {

    private lateinit var weatherRepository: WeatherRepository
    private lateinit var getCurrentWeatherUseCase: GetCurrentWeatherUseCase

    private val city = "Moscow"
    private val expectedWeather = CurrentWeather(
        time = "2025-05-06T15:00",
        interval = "900",
        temperature2m = "12.5",
        relativeHumidity2m = "67",
        apparentTemperature = "11.2",
        isDay = "1",
        precipitation = "0.1",
        rain = "0.1",
        showers = "0.0",
        snowfall = "0.0",
        weatherCode = "3",
        cloudCover = "65",
        windSpeed10m = "6.8",
        windDirection10m = "80",
        windGusts10m = "14.2"
    )

    @BeforeEach
    fun setup() {
        weatherRepository = mockk()
        getCurrentWeatherUseCase = GetCurrentWeatherUseCase(weatherRepository)
    }

    @Test
    fun `should return current weather for given city`() = runTest {
        coEvery { weatherRepository.getCurrentWeather(city) } returns expectedWeather

        val result = getCurrentWeatherUseCase(city)

        assertEquals(expectedWeather, result)
        coVerify(exactly = 1) { weatherRepository.getCurrentWeather(city) }
    }

    @Test
    fun `should throw exception when repository fails`() = runTest {
        val exceptionMessage = "API error"
        coEvery { weatherRepository.getCurrentWeather(city) } throws RuntimeException(exceptionMessage)

        try {
            getCurrentWeatherUseCase(city)
            fail("Exception was expected")
        } catch (e: RuntimeException) {
            assertEquals(exceptionMessage, e.message)
        }
    }

    @Test
    fun `should return weather with valid temperature value`() = runTest {
        val modifiedWeather = expectedWeather.copy(temperature2m = "17.8")
        coEvery { weatherRepository.getCurrentWeather(city) } returns modifiedWeather

        val result = getCurrentWeatherUseCase(city)

        assertEquals("17.8", result.temperature2m)
    }

    @Test
    fun `should call repository with empty city name`() = runTest {
        val emptyCity = ""
        coEvery { weatherRepository.getCurrentWeather(emptyCity) } returns expectedWeather

        val result = getCurrentWeatherUseCase(emptyCity)

        assertEquals(expectedWeather, result)
        coVerify { weatherRepository.getCurrentWeather(emptyCity) }
    }
}