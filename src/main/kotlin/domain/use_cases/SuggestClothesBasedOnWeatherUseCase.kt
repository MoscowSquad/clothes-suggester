package domain.use_cases

import domain.repository.WeatherRepository

class SuggestClothesBasedOnWeatherUseCase(
    private val weatherRepository: WeatherRepository
) {
    suspend fun getSuggestClothesByWeather(latitude: Double, longitude: Double): List<String>{
        val weather = weatherRepository.getCurrentWeather(latitude, longitude)
        val suggestions = mutableListOf<String>()
        val temperature = weather.temperature2m.toFloat()
        val windSpeed = weather.windSpeed10m.toFloat()
        val cloudCover = weather.cloudCover.toInt()
        val isDay = weather.isDay == 1
        val weatherCode = weather.weatherCode.toInt()
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
            temperature in 18f..25f -> {
                suggestions.addAll(listOf("T-shirt", "jeans"))
                if (!isDay) suggestions.add("light jacket")
            }
            temperature in 10f..18f -> {
                suggestions.addAll(listOf("long sleeves", "sweater", "hoodie"))
            }
            temperature in 0f..10f -> {
                suggestions.addAll(listOf("jacket", "coat", "layers"))
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

        // Cloud cover suggestions
        when {
            cloudCover > 70 -> suggestions.add("slightly warmer layers (overcast)")
            cloudCover in 30..70 -> suggestions.add("versatile layers (partly cloudy)")
            cloudCover < 30 -> suggestions.add("sun protection (clear skies)")
        }

        // Weather code specific suggestions
        when (weatherCode) {
            in 1..3 -> suggestions.add("light layers (partly cloudy)")
            in 45..48 -> suggestions.add("high-visibility clothing (fog)")
            in 51..67 -> suggestions.add("waterproof shoes (rain)")
            in 71..77 -> suggestions.add("snow gear")
            in 80..82 -> suggestions.add("rain poncho")
            in 95..99 -> suggestions.add("avoid metal accessories (thunderstorm)")
        }

        // Nighttime suggestion
        if (!isDay && temperature < 18) {
            suggestions.add("reflective clothing")
        }

        return suggestions.distinct()
    }
}