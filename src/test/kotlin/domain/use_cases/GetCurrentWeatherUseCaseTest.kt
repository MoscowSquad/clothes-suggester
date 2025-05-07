package domain.use_cases
import com.google.common.truth.Truth.assertThat
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
        weatherRepository = mockk(relaxed = true)
        getCurrentWeatherUseCase = GetCurrentWeatherUseCase(weatherRepository)
    }

    @Test
        fun `should return current weather for given city when getting the current weather`() = runTest {
            // Given
            coEvery { weatherRepository.getCurrentWeather(city) } returns expectedWeather

            // When
            val result = getCurrentWeatherUseCase(city)

        // Then
        coVerify(exactly = 1) { weatherRepository.getCurrentWeather(city) }
    }

    @Test
    fun `should throw exception when repository fails`() = runTest {
        // Given
        val exceptionMessage = "API error"
        coEvery { weatherRepository.getCurrentWeather(city) } throws RuntimeException(exceptionMessage)

        try {
            // When
            getCurrentWeatherUseCase(city)
            fail("Exception was expected")
        } catch (e: RuntimeException) {
            // Then
            assertThat(e.message).isEqualTo(exceptionMessage)
        }
    }

    @Test
    fun `should return weather with valid temperature value`() = runTest {
        // Given
        val modifiedWeather = expectedWeather.copy(temperature2m = "17.8")
        coEvery { weatherRepository.getCurrentWeather(city) } returns modifiedWeather

        // When
        val result = getCurrentWeatherUseCase(city)

        // Then
        assertThat(result.temperature2m).isEqualTo("17.8")
    }

    @Test
    fun `should call repository with empty city name`()= runTest {
        // Given
        val emptyCity = ""
        coEvery { weatherRepository.getCurrentWeather(emptyCity) } returns expectedWeather

        // When
        val result = getCurrentWeatherUseCase(emptyCity)

        // Then
        assertEquals(expectedWeather, result)
        coVerify { weatherRepository.getCurrentWeather(emptyCity) }
    }
}
