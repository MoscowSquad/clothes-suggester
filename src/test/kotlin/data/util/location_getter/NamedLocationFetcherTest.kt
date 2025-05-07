package data.util.location_getter

import com.google.common.truth.Truth
import domain.models.Location
import domain.models.NoLocationRetrieved
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class NamedLocationFetcherTest {
    private lateinit var httpClient: HttpClient
    private lateinit var locationFetcher: NamedLocationFetcher

    private fun setUp(handler: MockRequestHandleScope.(HttpRequestData) -> HttpResponseData) {
        val location = "Russia Moscow"
        val mockEngine = MockEngine(handler)
        httpClient = HttpClient(mockEngine)
        locationFetcher = NamedLocationFetcher(location, httpClient)
    }

    @Test
    fun `should return location when getting location return from the api`() = runTest {
        // Given
        setUp { _ ->
            respond(
                content = """{"lat":29.9791854, "lon":31.1316879, "city":"Giza","country":"Egypt"}""",
                status = HttpStatusCode.OK, headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        // When
        val result = locationFetcher.getLocation()

        // Then
        Truth.assertThat(result).isEqualTo(
            Location(29.9791854, 31.1316879, "Giza")
        )
    }

    @Test
    fun `should ignore other JSON keys when getting location return from the api`() =
        runTest {
            // Given
            setUp { _ ->
                respond(
                    content = """{"lat":29.9791854, "lon":31.1316879, "city":"Giza","country":"Egypt","timezone": "Africa/Cairo"}""",
                    status = HttpStatusCode.OK, headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }

            // When
            val result = locationFetcher.getLocation()

            // Then
            Truth.assertThat(result).isEqualTo(
                Location(29.9791854, 31.1316879, "Giza")
            )
        }

    @Test
    fun `should throw NoLocationRetrieved when error happen after request the api`() = runTest {
        // Given
        setUp { _ ->
            respond(
                content = """{"lat":29.9791854, "lon":31.1316879, "city":"Giza","country":"Egypt"}""",
                status = HttpStatusCode.NotFound, headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        // When, Then
        org.junit.jupiter.api.assertThrows<NoLocationRetrieved> {
            locationFetcher.getLocation()
        }
    }

    @Test
    fun `should throw NoLocationRetrieved when response not correct`() = runTest {
        // Given
        setUp { _ ->
            respond(
                content = """<html itemscope="" itemtype="http://schema.org/WebPage" lang="en">""",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        // When, Then
        org.junit.jupiter.api.assertThrows<NoLocationRetrieved> {
            locationFetcher.getLocation()
        }
    }
}