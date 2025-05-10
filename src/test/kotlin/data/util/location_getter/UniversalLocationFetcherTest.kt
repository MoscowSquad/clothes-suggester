package data.util.location_getter

import com.google.common.truth.Truth
import domain.models.Location
import domain.models.NoLocationRetrieved
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class UniversalLocationFetcherTest {
 private lateinit var httpClient: HttpClient
 private lateinit var locationFetcher: UniversalLocationFetcher

 @Test
 fun `getLocation() with CurrentLocationStrategy should return location when API succeeds`() = runTest {
  // Given
  val mockEngine = MockEngine { _ ->
   respond(
    content = """{"latitude":29.9791854, "longitude":31.1316879, "label":"The Great Pyramid of Giza"}""",
    status = HttpStatusCode.OK,
    headers = headersOf(HttpHeaders.ContentType, "application/json")
   )
  }
  httpClient = HttpClient(mockEngine)
  locationFetcher = UniversalLocationFetcher(
   UniversalLocationFetcher.CurrentLocationStrategy(),
   httpClient
  )

  // When
  val result = locationFetcher.getLocation()

  // Then
  Truth.assertThat(result).isEqualTo(
   Location(29.9791854, 31.1316879, "The Great Pyramid of Giza")
  )
 }

 @Test
 fun `getLocation() with CurrentLocationStrategy should ignore unknown JSON keys`() = runTest {
  // Given
  val mockEngine = MockEngine { _ ->
   respond(
    content = """{"latitude":29.9791854, "longitude":31.1316879, "city":"Giza","timezone":"Africa/Cairo"}""",
    status = HttpStatusCode.OK,
    headers = headersOf(HttpHeaders.ContentType, "application/json")
   )
  }
  httpClient = HttpClient(mockEngine)
  locationFetcher = UniversalLocationFetcher(
   UniversalLocationFetcher.CurrentLocationStrategy(),
   httpClient
  )

  // When
  val result = locationFetcher.getLocation()

  // Then
  Truth.assertThat(result).isEqualTo(
   Location(29.9791854, 31.1316879, "Giza")
  )
 }

 @Test
 fun `getLocation() with CurrentLocationStrategy should throw NoLocationRetrieved on API error`() = runTest {
  // Given
  val mockEngine = MockEngine { _ ->
   respond(
    content = """{"error":"Not found"}""",
    status = HttpStatusCode.NotFound,
    headers = headersOf(HttpHeaders.ContentType, "application/json")
   )
  }
  httpClient = HttpClient(mockEngine)
  locationFetcher = UniversalLocationFetcher(
   UniversalLocationFetcher.CurrentLocationStrategy(),
   httpClient
  )

  // When & Then
  assertThrows<NoLocationRetrieved> {
   locationFetcher.getLocation()
  }
 }

 @Test
 fun `getLocation() with NamedLocationStrategy should return location when API succeeds`() = runTest {
  // Given
  val mockEngine = MockEngine { request ->
   Truth.assertThat(request.url.toString()).contains("Moscow")
   respond(
    content = """{"data":[{"lat":55.7558, "lon":37.6176, "city":"Moscow","country":"Russia"}]}""",
    status = HttpStatusCode.OK,
    headers = headersOf(HttpHeaders.ContentType, "application/json")
   )
  }
  httpClient = HttpClient(mockEngine)
  locationFetcher = UniversalLocationFetcher(
   UniversalLocationFetcher.NamedLocationStrategy("Moscow"),
   httpClient
  )

  // When
  val result = locationFetcher.getLocation()

  // Then
  Truth.assertThat(result).isEqualTo(
   Location(55.7558, 37.6176, "Moscow")
  )
 }

 @Test
 fun `getLocation() with NamedLocationStrategy should ignore unknown JSON keys`() = runTest {
  // Given
  val mockEngine = MockEngine { _ ->
   respond(
    content = """{"data":[{"lat":55.7558, "lon":37.6176, "city":"Moscow","timezone":"Europe/Moscow"}]}""",
    status = HttpStatusCode.OK,
    headers = headersOf(HttpHeaders.ContentType, "application/json")
   )
  }
  httpClient = HttpClient(mockEngine)
  locationFetcher = UniversalLocationFetcher(
   UniversalLocationFetcher.NamedLocationStrategy("Moscow"),
   httpClient
  )

  // When
  val result = locationFetcher.getLocation()

  // Then
  Truth.assertThat(result).isEqualTo(
   Location(55.7558, 37.6176, "Moscow")
  )
 }

 @Test
 fun `getLocation() with NamedLocationStrategy should throw NoLocationRetrieved on API error`() = runTest {
  // Given
  val mockEngine = MockEngine { _ ->
   respond(
    content = """{"error":"Not found"}""",
    status = HttpStatusCode.NotFound,
    headers = headersOf(HttpHeaders.ContentType, "application/json")
   )
  }
  httpClient = HttpClient(mockEngine)
  locationFetcher = UniversalLocationFetcher(
   UniversalLocationFetcher.NamedLocationStrategy("InvalidPlace"),
   httpClient
  )

  // When & Then
  assertThrows<NoLocationRetrieved> {
   locationFetcher.getLocation()
  }
 }

 @Test
 fun `getLocation() with NamedLocationStrategy should throw NoLocationRetrieved on malformed response`() = runTest {
  // Given
  val mockEngine = MockEngine { _ ->
   respond(
    content = """<html>Not a JSON</html>""",
    status = HttpStatusCode.OK,
    headers = headersOf(HttpHeaders.ContentType, "application/json")
   )
  }
  httpClient = HttpClient(mockEngine)
  locationFetcher = UniversalLocationFetcher(
   UniversalLocationFetcher.NamedLocationStrategy("Moscow"),
   httpClient
  )

  // When & Then
  assertThrows<NoLocationRetrieved> {
   locationFetcher.getLocation()
  }
 }

 @Test
 fun `getLocation() with CurrentLocationStrategy should throw NoLocationRetrieved on empty API response`() = runTest {
  // Given
  val mockEngine = MockEngine { _ ->
   respond(
    content = "",
    status = HttpStatusCode.OK,
    headers = headersOf(HttpHeaders.ContentType, "application/json")
   )
  }
  httpClient = HttpClient(mockEngine)
  locationFetcher = UniversalLocationFetcher(
   UniversalLocationFetcher.CurrentLocationStrategy(),
   httpClient
  )

  // When & Then
  assertThrows<NoLocationRetrieved> {
   locationFetcher.getLocation()
  }
 }

 @Test
 fun `getLocation() with CurrentLocationStrategy should throw NoLocationRetrieved if JSON misses required fields`() = runTest {
  // Given
  val mockEngine = MockEngine { _ ->
   respond(
    content = """{"city": "Giza"}""",
    status = HttpStatusCode.OK,
    headers = headersOf(HttpHeaders.ContentType, "application/json")
   )
  }
  httpClient = HttpClient(mockEngine)
  locationFetcher = UniversalLocationFetcher(
   UniversalLocationFetcher.CurrentLocationStrategy(),
   httpClient
  )

  // When & Then
  assertThrows<NoLocationRetrieved> {
   locationFetcher.getLocation()
  }
 }

 @Test
 fun `getLocation() with NamedLocationStrategy should encode special characters in URL`() = runTest {
  // Given
  val mockEngine = MockEngine { request ->
   Truth.assertThat(request.url.toString()).contains("New%20York")
   respond(
    content = """{"data":[{"lat":40.7128, "lon":-74.0060, "city":"New York"}]}""",
    status = HttpStatusCode.OK,
    headers = headersOf(HttpHeaders.ContentType, "application/json")
   )
  }
  httpClient = HttpClient(mockEngine)
  locationFetcher = UniversalLocationFetcher(
   UniversalLocationFetcher.NamedLocationStrategy("New York"),
   httpClient
  )

  // When
  val result = locationFetcher.getLocation()

  // Then
  Truth.assertThat(result).isEqualTo(
   Location(40.7128, -74.0060, "New York")
  )
 }

 @Test
 fun `getLocation() with NamedLocationStrategy should throw NoLocationRetrieved if 'data' array is empty`() = runTest {
  // Given
  val mockEngine = MockEngine { _ ->
   respond(
    content = """{"data": []}""",
    status = HttpStatusCode.OK,
    headers = headersOf(HttpHeaders.ContentType, "application/json")
   )
  }
  httpClient = HttpClient(mockEngine)
  locationFetcher = UniversalLocationFetcher(
   UniversalLocationFetcher.NamedLocationStrategy("UnknownPlace"),
   httpClient
  )

  // When & Then
  assertThrows<NoLocationRetrieved> {
   locationFetcher.getLocation()
  }
 }

 @Test
 fun `getLocation() should handle null values in data array`() = runTest {
  // Given
  val mockEngine = MockEngine { _ ->
   respond(
    content = """{"data":[null,{"lat":1.0,"lon":2.0,"city":"test"}]}""",
    status = HttpStatusCode.OK,
    headers = headersOf(HttpHeaders.ContentType, "application/json")
   )
  }
  httpClient = HttpClient(mockEngine)
  locationFetcher = UniversalLocationFetcher(
   UniversalLocationFetcher.NamedLocationStrategy("test"),
   httpClient
  )

  // When & Then
  assertThrows<NoLocationRetrieved> {
   locationFetcher.getLocation()
  }
 }

 @Test
 fun `getLocation() should handle malformed JSON in data array element`() = runTest {
  // Given
  val mockEngine = MockEngine { _ ->
   respond(
    content = """{"data":["not-a-valid-json-object"]}""",
    status = HttpStatusCode.OK,
    headers = headersOf(HttpHeaders.ContentType, "application/json")
   )
  }
  httpClient = HttpClient(mockEngine)
  locationFetcher = UniversalLocationFetcher(
   UniversalLocationFetcher.NamedLocationStrategy("test"),
   httpClient
  )

  // When & Then
  assertThrows<NoLocationRetrieved> {
   locationFetcher.getLocation()
  }
 }

 @Test
 fun `getLocation() should handle extremely large JSON objects`() = runTest {
  // Given
  val largeJson = buildString {
   append("""{"data":[""")
   repeat(1000) {
    append("""{"lat":${it.toDouble()},"lon":${it.toDouble()},"city":"City$it"}""")
    if (it < 999) append(",")
   }
   append("]}")
  }

  val mockEngine = MockEngine { _ ->
   respond(
    content = largeJson,
    status = HttpStatusCode.OK,
    headers = headersOf(HttpHeaders.ContentType, "application/json")
   )
  }
  httpClient = HttpClient(mockEngine)
  locationFetcher = UniversalLocationFetcher(
   UniversalLocationFetcher.NamedLocationStrategy("test"),
   httpClient
  )

  // When
  val result = locationFetcher.getLocation()

  // Then
  Truth.assertThat(result).isNotNull()
 }
}