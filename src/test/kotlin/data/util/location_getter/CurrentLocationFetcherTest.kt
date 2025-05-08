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
import org.junit.jupiter.api.assertThrows

class CurrentLocationFetcherTest {
    private lateinit var httpClient: HttpClient
    private lateinit var locationFetcher: CurrentLocationFetcher

    private fun setUp(handler: MockRequestHandleScope.(HttpRequestData) -> HttpResponseData) {
        val mockEngine = MockEngine(handler)
        httpClient = HttpClient(mockEngine)
        locationFetcher = CurrentLocationFetcher(httpClient)
    }

    @Test
    fun `getLocation() should return location when getting location return from the api`() = runTest {
        // Given
        setUp { _ ->
            respond(
                content = """{"latitude":29.9791854, "longitude":31.1316879, "label":"The Great Pyramid of Giza"}""",
                status = HttpStatusCode.OK, headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        // When
        val result = locationFetcher.getLocation()

        // Then
        Truth.assertThat(result).isEqualTo(
            Location(29.9791854, 31.1316879, "The Great Pyramid of Giza")
        )
    }

    @Test
    fun `getLocation() should ignore other JSON keys when getting location return from the api`() = runTest {
        // Given
        setUp { _ ->
            respond(
                content = """{"latitude":29.9791854, "longitude":31.1316879, "city":"The Great Pyramid of Giza","timezone": "Africa/Cairo"}""",
                status = HttpStatusCode.OK, headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        // When
        val result = locationFetcher.getLocation()

        // Then
        Truth.assertThat(result).isEqualTo(
            Location(29.9791854, 31.1316879, "The Great Pyramid of Giza")
        )
    }

    @Test
    fun `getLocation() should throw NoLocationRetrieved when error happen after request the api`() = runTest {
        // Given
        setUp { _ ->
            respond(
                content = """{"latitude":29.9791854, "longitude":31.1316879, "label":"The Great Pyramid of Giza"}""",
                status = HttpStatusCode.NotFound, headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        // When, Then
        assertThrows<NoLocationRetrieved> {
            locationFetcher.getLocation()
        }
    }

    @Test
    fun `getLocation() should throw NoLocationRetrieved when response not correct`() = runTest {
        // Given
        setUp { _ ->
            respond(
                content = """<html itemscope="" itemtype="http://schema.org/WebPage" lang="en">""",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        // When, Then
        assertThrows<NoLocationRetrieved> {
            locationFetcher.getLocation()
        }
    }
}