package domain.use_cases

import domain.models.CurrentWeather

class SuggestClothesBasedOnWeatherUseCase(
    private val weather: CurrentWeather
) {
}