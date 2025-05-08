package presentation

import data.util.location_getter.CurrentLocationFetcher
import data.util.location_getter.NamedLocationFetcher
import domain.use_cases.GetCurrentWeatherUseCase
import domain.use_cases.GetLocationUseCase
import domain.use_cases.SuggestClothesBasedOnWeatherUseCase
import io.ktor.client.*
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.io.ConsoleIO

class ClothesSuggesterConsoleUITest {
    private lateinit var httpClient: HttpClient
    private lateinit var getLocationUseCase: GetLocationUseCase
    private lateinit var getCurrentWeatherUseCase: GetCurrentWeatherUseCase
    private lateinit var suggestClothesBasedOnWeatherUseCase: SuggestClothesBasedOnWeatherUseCase
    private lateinit var consoleIO: ConsoleIO

    private lateinit var clothesSuggesterConsoleUI: ClothesSuggesterConsoleUI

    @BeforeEach
    fun setUp() {
        httpClient = mockk(relaxed = true)
        getLocationUseCase = mockk(relaxed = true)
        getCurrentWeatherUseCase = mockk(relaxed = true)
        suggestClothesBasedOnWeatherUseCase = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)
        clothesSuggesterConsoleUI = ClothesSuggesterConsoleUI(
            httpClient,
            getLocationUseCase,
            getCurrentWeatherUseCase,
            suggestClothesBasedOnWeatherUseCase,
            consoleIO
        )
    }


    @Test
    fun `should display welcome message and authenticate user on start`() = runTest {
        // Given
        coEvery { consoleIO.writeln(any()) } just Runs
        coEvery { consoleIO.read() } returns "100"

        // When
        clothesSuggesterConsoleUI.start()

        // Then
        coVerify {
            consoleIO.writeln(match { it.contains("Welcome to Clothes Suggester") })
        }
    }

    @Test
    fun `should display main menu options`() = runTest {
        // Given
        coEvery { consoleIO.read() } returns "0" // Exit option

        // When
        clothesSuggesterConsoleUI.start()

        // Then
        coVerify {
            consoleIO.write(match { it.contains("1. my current location") })
            consoleIO.write(match { it.contains("2. another location") })
            consoleIO.write(match { it.contains("3. exit") })
            consoleIO.read()
        }
    }

    @Test
    fun `should navigate to get current location screen when option 1 is selected`() = runTest {
        // Given
        coEvery { consoleIO.read() } returns "1"

        // When
        clothesSuggesterConsoleUI.start()

        // Then
        coVerify {
            getLocationUseCase.getLocation(any<CurrentLocationFetcher>())
        }
    }

    @Test
    fun `should navigate to get location by name when option 2 is selected`() = runTest {
        // Given
        coEvery { consoleIO.read() } returns "2"

        // When
        clothesSuggesterConsoleUI.start()

        // Then
        coVerify {
            getLocationUseCase.getLocation(any<NamedLocationFetcher>())
        }
    }
}