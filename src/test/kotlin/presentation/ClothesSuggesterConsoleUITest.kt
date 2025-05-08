package presentation

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
}