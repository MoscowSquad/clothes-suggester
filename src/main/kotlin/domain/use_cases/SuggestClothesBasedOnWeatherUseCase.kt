package domain.use_cases

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
            temperature < 0 -> {
                suggestions.addAll(listOf("heavy winter coat", "gloves", "scarf"))
            }

            temperature <= 29 -> {
                suggestions.addAll(listOf("light T-shirt", "shorts", "breathable wear"))
            }

            else -> {
                suggestions.addAll(listOf("very light clothes", "tank tops", "shorts"))
            }
        }

        // Wind-based suggestions
        when {
            windSpeed > 15 -> suggestions.add("windbreaker")
            windSpeed > 8 -> suggestions.add("light jacket")
        }

        // Precipitation-based suggestions
        when {
            rain > 0 -> suggestions.addAll(listOf("waterproof jacket", "umbrella"))
            snowfall > 0 -> suggestions.addAll(listOf("snow boots", "insulated gloves"))
        }

        // Nighttime suggestion
        if (!isDay) {
            suggestions.add("reflective clothing")
        }

        return suggestions.distinct()
    }
}