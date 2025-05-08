package domain.use_cases

import WeatherRepository
import com.google.common.truth.Truth
import domain.models.CurrentWeather
import org.junit.jupiter.api.Test
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach

class SuggestClothesBasedOnWeatherUseCaseTest {
    private lateinit var repository: WeatherRepository
    private lateinit var useCase: SuggestClothesBasedOnWeatherUseCase

    @BeforeEach
    fun setUp() {
        repository = mockk(relaxed = true)
        useCase = SuggestClothesBasedOnWeatherUseCase()
    }

    @Test
    fun `getSuggestClothesByWeather() should suggest very light clothes when temperature is greater than 30 C`() =
        runTest {
            // Given
            val weather = createWeather(temperature2m = 32.0)

            coEvery { repository.getCurrentWeather(any(), any()) } returns weather

            // When
            val suggestions = useCase.getSuggestClothesByWeather(weather)

            // Then
            assertThat(suggestions).containsAtLeast(
                "very light clothes",
                "tank tops",
                "shorts"
            )
        }

    @Test
    fun `getSuggestClothesByWeather() should suggest light T-shirt when temperature between 25-30 C`() = runTest {
        // Given
        val weather = createWeather(temperature2m = 27.0)

        coEvery { repository.getCurrentWeather(any(), any()) } returns weather

        // When
        val suggestions = useCase.getSuggestClothesByWeather(weather)

        // Then
        assertThat(suggestions).containsAtLeast(
            "light T-shirt",
            "shorts",
            "breathable wear"
        )
    }

    @Test
    fun `getSuggestClothesByWeather() should suggest jacket when temperature is less than 0 C`() = runTest {
        // Given
        val weather = createWeather(temperature2m = -5.0)

        coEvery { repository.getCurrentWeather(any(), any()) } returns weather

        // When
        val suggestions = useCase.getSuggestClothesByWeather(weather)

        // Then
        assertThat(suggestions).containsAtLeast(
            "heavy winter coat",
            "gloves",
            "scarf"
        )
    }

    @Test
    fun `getSuggestClothesByWeather() should suggest windbreaker when wind speed is greater than 15 kmh`() = runTest {
        // Given
        val weather = createWeather(windSpeed10m = 17.0)
        coEvery { repository.getCurrentWeather(any(), any()) } returns weather

        // When
        val suggestions = useCase.getSuggestClothesByWeather(weather)

        // Then
        assertThat(suggestions).contains("windbreaker")
    }

    @Test
    fun `getSuggestClothesByWeather() should suggest light jacket when wind speed 8-15 kmh`() = runTest {
        // Given
        val weather = createWeather(windSpeed10m = 10.0)
        coEvery { repository.getCurrentWeather(any(), any()) } returns weather

        // When
        val suggestions = useCase.getSuggestClothesByWeather(weather)

        // Then
        assertThat(suggestions).contains("light jacket")
    }

    @Test
    fun `getSuggestClothesByWeather() should suggest waterproof jacket or umbrella when rain is greater than 0 mm`() =
        runTest {
            // Given
            val weather = createWeather(rain = 2.00)

            coEvery { repository.getCurrentWeather(any(), any()) } returns weather

            // When
            val suggestions = useCase.getSuggestClothesByWeather(weather)

            // Then
            assertThat(suggestions).containsAtLeast("waterproof jacket", "umbrella")
        }

    @Test
    fun `getSuggestClothesByWeather() should suggest snow boots or insulated gloves when snowfall is greater than 0 cm`() =
        runTest {
            // Given
            val weather = createWeather(snowfall = 3.00)

            coEvery { repository.getCurrentWeather(any(), any()) } returns weather

            // When
            val suggestions = useCase.getSuggestClothesByWeather(weather)

            // Then
            assertThat(suggestions).containsAtLeast("snow boots", "insulated gloves")
        }

    @Test
    fun `getSuggestClothesByWeather() should suggest reflective clothing layers at night and temperature is less than 18`() =
        runTest {
            // Given
            val weather = createWeather(isDay = 0, temperature2m = 15.0)

            coEvery { repository.getCurrentWeather(any(), any()) } returns weather

            // When
            val suggestions = useCase.getSuggestClothesByWeather(weather)

            // Then
            assertThat(suggestions).contains("reflective clothing")
        }

    fun createWeather(
        temperature2m: Double = 32.0,
        windSpeed10m: Double = 5.0,
        cloudCover: Double = 20.0,
        isDay: Int = 1,
        relativeHumidity2m: Double = 50.0,
        apparentTemperature: Double = 33.0,
        rain: Double = 0.0,
        snowfall: Double = 0.0,
        weatherCode: Double = 1.0,
        time: String = "2025-05-07T13:45",
        interval: Double = 900.0
    ): CurrentWeather {
        return CurrentWeather(
            temperature2m = temperature2m,
            windSpeed10m = windSpeed10m,
            cloudCover = cloudCover,
            isDay = isDay,
            relativeHumidity2m = relativeHumidity2m,
            apparentTemperature = apparentTemperature,
            rain = rain,
            snowfall = snowfall,
        )
    }
}