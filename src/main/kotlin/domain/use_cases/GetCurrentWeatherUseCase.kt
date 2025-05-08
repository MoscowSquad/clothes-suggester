package domain.use_cases
import domain.models.CurrentWeather
import domain.repository.WeatherRepository

class GetCurrentWeatherUseCase(private val repository: WeatherRepository) {
    suspend fun getCurrentWeather(latitude: Double, longitude: Double): CurrentWeather {
        return repository.getCurrentWeather(latitude, longitude)
    }
}
