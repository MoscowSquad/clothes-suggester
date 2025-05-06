package domain.repository

import domain.models.CurrentWeather

interface WeatherRepository {
     suspend fun getCurrentWeather(city: String): CurrentWeather
}