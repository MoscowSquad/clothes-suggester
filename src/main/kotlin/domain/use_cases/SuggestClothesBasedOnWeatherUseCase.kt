package domain.use_cases

import WeatherRepository
import domain.models.CurrentWeather

class SuggestClothesBasedOnWeatherUseCase {
    suspend fun getSuggestClothesByWeather(weather: CurrentWeather): List<String> {
        val suggestions = mutableListOf<String>()
        val temperature = weather.temperature2m.toFloat()
        val windSpeed = weather.windSpeed10m.toFloat()
        val isDay = weather.isDay == 1
        val rain = weather.rain.toFloat()
        val snowfall = weather.snowfall.toFloat()

        // Temperature-based suggestions
        when {
            temperature > 30 -> {
                suggestions.addAll(listOf("very light clothes", "tank tops", "shorts"))
            }

            temperature in 25f..30f -> {
                suggestions.addAll(listOf("light T-shirt", "shorts", "breathable wear"))
            }

            temperature < 0 -> {
                suggestions.addAll(listOf("heavy winter coat", "gloves", "scarf"))
            }
        }

        // Wind-based suggestions
        when {
            windSpeed > 15 -> suggestions.add("windbreaker")
            windSpeed in 8f..15f -> suggestions.add("light jacket")
        }

        // Precipitation-based suggestions
        when {
            rain > 0 -> suggestions.addAll(listOf("waterproof jacket", "umbrella"))
            snowfall > 0 -> suggestions.addAll(listOf("snow boots", "insulated gloves"))
        }

        // Nighttime suggestion
        if (!isDay && temperature < 18) {
            suggestions.add("reflective clothing")
        }

        return suggestions.distinct()
    }
}