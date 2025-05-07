import domain.models.CurrentWeather
class GetCurrentWeatherUseCase(private val repository: WeatherRepository) {
    suspend operator fun invoke(latitude: Double, longitude: Double): CurrentWeather {
        return repository.getCurrentWeather(latitude, longitude)
    }
}
