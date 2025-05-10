package presentation

import data.util.location_getter.UniversalLocationFetcher
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
            consoleIO.writeln(match { it.contains("Welcome to Clothes Suggester App") })
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
    fun `should use CurrentLocationStrategy when option 1 is selected`() = runTest {
        // Given
        coEvery { consoleIO.read() } returns "1"
        coEvery { getLocationUseCase.getLocation(any()) } returns mockk()

        // When
        clothesSuggesterConsoleUI.start()

        // Then
        coVerify {
            getLocationUseCase.getLocation(
                match { fetcher ->
                    fetcher is UniversalLocationFetcher &&
                            fetcher.strategy is UniversalLocationFetcher.CurrentLocationStrategy
                }
            )
        }
    }

    @Test
    fun `should use NamedLocationStrategy when option 2 is selected`() = runTest {
        // Given
        coEvery { consoleIO.read() } returns "2"
        coEvery { consoleIO.read() } returns "New York"
        coEvery { getLocationUseCase.getLocation(any()) } returns mockk()

        // When
        clothesSuggesterConsoleUI.start()

        // Then
        coVerify {
            getLocationUseCase.getLocation(
                match { fetcher ->
                    fetcher is UniversalLocationFetcher &&
                            fetcher.strategy is UniversalLocationFetcher.NamedLocationStrategy
                }
            )
        }
    }

    @Test
    fun `should pass correct location name to NamedLocationStrategy`() = runTest {
        // Given
        coEvery { consoleIO.read() } returnsMany listOf("2", "Paris")
        coEvery { getLocationUseCase.getLocation(any()) } returns mockk()

        // When
        clothesSuggesterConsoleUI.start()

        // Then
        coVerify {
            getLocationUseCase.getLocation(
                match { fetcher ->
                    fetcher is UniversalLocationFetcher &&
                            (fetcher.strategy as UniversalLocationFetcher.NamedLocationStrategy).placeName == "Paris"
                }
            )
        }
    }
}