package domain.repository

import domain.models.CurrentWeather

interface WeatherRepository {
    suspend fun getCurrentWeather(latitude: Double, longitude: Double): CurrentWeather
}