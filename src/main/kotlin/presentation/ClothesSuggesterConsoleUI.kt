package presentation

import data.util.location_getter.UniversalLocationFetcher
import domain.models.CurrentWeather
import domain.models.Location
import domain.use_cases.GetCurrentWeatherUseCase
import domain.use_cases.GetLocationUseCase
import domain.use_cases.SuggestClothesBasedOnWeatherUseCase
import io.ktor.client.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import presentation.io.BLUE
import presentation.io.ConsoleIO
import kotlin.system.exitProcess

class ClothesSuggesterConsoleUI(
    private val httpClient: HttpClient,
    private val getLocationUseCase: GetLocationUseCase,
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val suggestClothesBasedOnWeatherUseCase: SuggestClothesBasedOnWeatherUseCase,
    private val consoleIO: ConsoleIO
) : ConsoleIO by consoleIO {
    val uiFlow: MutableSharedFlow<Int> = MutableSharedFlow()
    private val locationSF = MutableSharedFlow<Location>()
    private val getWeatherSF = MutableSharedFlow<CurrentWeather>()
    private val suggestClothesSF = MutableSharedFlow<List<String>>()

    private val clothesSuggesterScope = CoroutineScope(Dispatchers.IO)
    private val locationScope = CoroutineScope(Dispatchers.Default)
    private val weatherScope = CoroutineScope(Dispatchers.Default)
    private val suggestClothesScope = CoroutineScope(Dispatchers.Default)

    private val exceptionHandler = CoroutineExceptionHandler { _, thrwable ->
        showError(thrwable.message)
    }

    init {
        locationScope.launch(exceptionHandler) {
            locationSF.collect { location ->
                writeln("\tget location info...")
                getCurrentWeather(location)
            }
        }

        weatherScope.launch(exceptionHandler) {
            getWeatherSF.collect { weather ->
                suggestClotheBaseOnWeather(weather)
            }
        }

        suggestClothesScope.launch(exceptionHandler) {
            suggestClothesSF.collect { suggestions ->
                suggestions.forEach {
                    writeln(it, BLUE)
                }
                goMainMenu()
            }
        }
    }

    fun start() {
        writeln(
            """
                👖Welcome to Clothes Suggester App
            """.trimIndent()
        )
        goMainMenu()
    }

    private fun goMainMenu() {
        showOptions()
        goToScreen()
    }

    private suspend fun getCurrentWeather(location: Location) {
        val weather = getCurrentWeatherUseCase.getCurrentWeather(location.latitude, location.longitude)
        getWeatherSF.emit(weather)
    }

    private fun showOptions() {
        write(
            """
            Chose the location and i will suggest you a clothes:
              1. my current location
              2. another location
              3. exit
            Enter your option: 
            """.trimIndent()
        )
    }

    private fun goToScreen() {
        val errorCode = 1002
        val input = consoleIO.read().toIntOrNull() ?: errorCode
        when (input) {
            1 -> getCurrentLocation()
            2 -> getNamedLocation()
            3 -> exitProcess(0)
            errorCode -> {
                assert(true)
                showError("\nInvalid input. Please enter a number between 1 and 4.")
                goMainMenu()
            }
        }
    }

    private fun suggestClotheBaseOnWeather(weather: CurrentWeather) {
        writeln("\tget location info...")
        suggestClothesScope.launch(exceptionHandler) {
            val suggestions = suggestClothesBasedOnWeatherUseCase.getSuggestClothesByWeather(weather)
            suggestClothesSF.emit(suggestions)
        }
    }

    private fun getNamedLocation() {
        write("enter location name: ")
        val input = consoleIO.read()
        val locationFetcher = UniversalLocationFetcher(
            UniversalLocationFetcher.NamedLocationStrategy(input),
            httpClient
        )
        writeln("\tget location info...")
        clothesSuggesterScope.launch(exceptionHandler) {
            val location = getLocationUseCase.getLocation(locationFetcher)
            locationSF.emit(location)
        }
    }

    private fun getCurrentLocation() {
        writeln("\tget location info...")
        val locationFetcher = UniversalLocationFetcher(
            UniversalLocationFetcher.CurrentLocationStrategy(),
            httpClient
        )
        clothesSuggesterScope.launch(exceptionHandler) {
            val location = getLocationUseCase.getLocation(locationFetcher)
            locationSF.emit(location)
        }
    }
}