package domain.use_cases

import domain.models.CurrentWeather
import domain.repository.WeatherRepository
import org.junit.jupiter.api.Test
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach

class SuggestClothesBasedOnWeatherUseCaseTest {
 private lateinit var repository: WeatherRepository
 private lateinit var useCase: SuggestClothesBasedOnWeatherUseCase

 @BeforeEach
 fun setUp() {
  repository = mockk(relaxed = true)
  useCase = SuggestClothesBasedOnWeatherUseCase(repository)
 }

 @Test
 fun `getSuggestClothesByWeather() should suggest very light clothes when temperature is greater than 30 C`() = runTest {
  // Given
  val weather = createWeather(temperature2m = 32.0)

  coEvery { repository.getCurrentWeather(any(), any()) } returns weather

  // When
  val suggestions = useCase.getSuggestClothesByWeather(52.52,13.419998)

  // Then
  assertThat(suggestions).containsAtLeast(
    "very light clothes",
    "tank tops",
    "shorts"
   )
 }

 @Test
 fun `getSuggestClothesByWeather() should suggest light T-shirt when temperature between 25-30 C`() = runTest{
  // Given
  val weather = createWeather(temperature2m = 27.0)

  coEvery { repository.getCurrentWeather(any(), any()) } returns weather

  // When
  val suggestions = useCase.getSuggestClothesByWeather(52.52,13.419998)

  // Then
  assertThat(suggestions).containsAtLeast(
   "light T-shirt",
   "shorts",
   "breathable wear"
  )
 }

 @Test
 fun `getSuggestClothesByWeather() should suggest T-shirt and jeans when temperature between 18-25 C`() = runTest {
  // Given
  val weather = createWeather(temperature2m = 20.0)

  coEvery { repository.getCurrentWeather(any(), any()) } returns weather

  // When
  val suggestions = useCase.getSuggestClothesByWeather(52.52,13.419998)

  // Then
  assertThat(suggestions).containsAtLeast(
   "T-shirt",
   "jeans",
   "light jacket"
  )
 }

 @Test
 fun `getSuggestClothesByWeather() should suggest sweater when temperature between 10-18 C`() = runTest {
  // Given
  val weather = createWeather(temperature2m = 15.0)

  coEvery { repository.getCurrentWeather(any(), any()) } returns weather

  // When
  val suggestions = useCase.getSuggestClothesByWeather(52.52,13.419998)

  // Then
  assertThat(suggestions).containsAtLeast(
   "Long sleeves",
   "sweater",
   "hoodie"
  )
 }

 @Test
 fun `getSuggestClothesByWeather() should suggest jacket when temperature between 0-10 C`() = runTest {
  // Given
  val weather = createWeather(temperature2m = 5.0)

  coEvery { repository.getCurrentWeather(any(), any()) } returns weather

  // When
  val suggestions = useCase.getSuggestClothesByWeather(52.52,13.419998)

  // Then
  assertThat(suggestions).containsAtLeast(
   "Jacket",
   "coat",
   "layers"
  )
 }

 @Test
 fun `getSuggestClothesByWeather() should suggest jacket when temperature is less than 0 C`() = runTest {
  // Given
  val weather = createWeather(temperature2m = -5.0)

  coEvery { repository.getCurrentWeather(any(), any()) } returns weather

  // When
  val suggestions = useCase.getSuggestClothesByWeather(52.52,13.419998)

  // Then
  assertThat(suggestions).containsAtLeast(
   "heavy winter coat",
   "gloves",
   "scarf"
  )
 }

 @Test
 fun `getSuggestClothesByWeather() should suggest windbreaker when wind speed is greater than 15 kmh`() = runTest {
  // Given
  val weather = createWeather(windSpeed10m = 17.0)
  coEvery { repository.getCurrentWeather(any(), any()) } returns weather

  // When
  val suggestions = useCase.getSuggestClothesByWeather(52.52,13.419998)

  // Then
  assertThat(suggestions).containsAtLeast(
   "windbreaker",
   "hoodie"
  )
 }

 @Test
 fun `getSuggestClothesByWeather() should suggest light jacket when wind speed 8-15 kmh`() = runTest {
  // Given
  val weather = createWeather(windSpeed10m = 10.0)
  coEvery { repository.getCurrentWeather(any(), any()) } returns weather

  // When
  val suggestions = useCase.getSuggestClothesByWeather(52.52,13.419998)

  // Then
  assertThat(suggestions).contains("light jacket")
 }

 @Test
 fun `getSuggestClothesByWeather() should suggest no impact when wind speed is less than 8 kmh`() = runTest {
  // Given
  val weather = createWeather(windSpeed10m = 5.0)

  coEvery { repository.getCurrentWeather(any(), any()) } returns weather

  // When
  val suggestions = useCase.getSuggestClothesByWeather(52.52,13.419998)

  // Then
  assertThat(suggestions).contains("no impact")
 }

 @Test
 fun `getSuggestClothesByWeather() should suggest warmer layers at night`() = runTest {
  // Given
  val weather = createWeather(isDay = 0)

  coEvery { repository.getCurrentWeather(any(), any()) } returns weather

  // When
  val suggestions = useCase.getSuggestClothesByWeather(52.52,13.419998)

  // Then
  assertThat(suggestions).contains("slightly warmer layers")
 }

 @Test
 fun `getSuggestClothesByWeather() should suggest cooler feel when overcast`() = runTest {
  // Given
  val weather = createWeather(cloudCover = 70.0)
  coEvery { repository.getCurrentWeather(any(), any()) } returns weather

  // When
  val suggestions = useCase.getSuggestClothesByWeather(52.52,13.419998)

  // Then
  assertThat(suggestions).contains("cooler feel")
 }

 @Test
 fun `getSuggestClothesByWeather() should suggest partly cloudy feel when cloud cover 30-60%`() = runTest {
  // Given
  val weather = createWeather(cloudCover = 50.0)
  coEvery { repository.getCurrentWeather(any(), any()) } returns weather

  // When
  val suggestions = useCase.getSuggestClothesByWeather(52.52,13.419998)

  // Then
  assertThat(suggestions).contains("partly cloudy feel")
 }

 @Test
 fun `getSuggestClothesByWeather() should suggest mostly clear feel when cloud cover is less than 30%`() = runTest {
  // Given
  val weather = createWeather(cloudCover = 25.0)
  coEvery { repository.getCurrentWeather(any(), any()) } returns weather

  // When
  val suggestions = useCase.getSuggestClothesByWeather(52.52,13.419998)

  // Then
  assertThat(suggestions).contains("mostly clear feel")
 }


 fun createWeather(
  temperature2m: Double = 32.0,
  windSpeed10m: Double = 5.0,
  cloudCover: Double = 20.0,
  isDay: Int = 1,
  relativeHumidity2m: Double = 50.0,
  apparentTemperature: Double = 33.0,
  rain: Double = 0.0,
  snowfall: Double = 0.0,
  weatherCode: Double = 1.0,
  time: String = "2025-05-07T13:45",
  interval: Double = 900.0
 ): CurrentWeather {
  return CurrentWeather(
   temperature2m = temperature2m,
   windSpeed10m = windSpeed10m,
   cloudCover = cloudCover,
   isDay = isDay,
   relativeHumidity2m = relativeHumidity2m,
   apparentTemperature = apparentTemperature,
   rain = rain,
   snowfall = snowfall,
   weatherCode = weatherCode,
   time = time,
   interval = interval
  )
 }
}