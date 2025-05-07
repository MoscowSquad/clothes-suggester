import domain.models.CurrentWeather
class GetCurrentWeatherUseCase(private val repository: WeatherRepository) {
    suspend fun getCurrentWeather(latitude: Double, longitude: Double): CurrentWeather {
        return repository.getCurrentWeather(latitude, longitude)
    }
}
