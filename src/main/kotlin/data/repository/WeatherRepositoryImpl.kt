package data.repository

import domain.models.CurrentWeather
import domain.repository.WeatherRepository

class WeatherRepositoryImpl : WeatherRepository{
    override suspend fun getCurrentWeather(city: String): CurrentWeather {
        TODO("Not yet implemented")
    }

}