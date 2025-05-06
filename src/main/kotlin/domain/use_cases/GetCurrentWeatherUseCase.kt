package domain.use_cases

import domain.models.CurrentWeather
import domain.repository.WeatherRepository

class GetCurrentWeatherUseCase(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(city: String): CurrentWeather {
        return weatherRepository.getCurrentWeather(city)
    }
}
