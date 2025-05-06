package data.util.location_getter

import com.google.common.truth.Truth
import domain.models.Location
import domain.models.NoLocationRetrieved
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CurrentLocationFetcherTest {
    private lateinit var httpClient: HttpClient
    private lateinit var httpResponse: HttpResponse
    private lateinit var locationFetcher: CurrentLocationFetcher

    @BeforeEach
    fun setUp() {
        httpClient = mockk()
        httpResponse = mockk()

        locationFetcher = CurrentLocationFetcher(this.httpClient)
    }

    @Test
    fun `should return location when getting location return from the api`() = runTest {
        // Given
        val mockFunc: HttpRequestBuilder.() -> Unit = mockk(relaxed = true)
        coEvery { httpResponse.status } returns HttpStatusCode.OK
        coEvery { mockFunc.invoke(any()) } returns Unit
        coEvery { httpClient.get(any<String>(), mockFunc) } returns httpResponse
        coEvery { httpResponse.bodyAsText() } returns """
            {"latitude":29.9791854, "longitude":31.1316879, "label":"The Great Pyramid of Giza"}
        """.trimIndent()

        // When
        val result = locationFetcher.getLocation()

        // Then
        Truth.assertThat(result)
            .isEqualTo(Location(29.9791854, 31.1316879, "The Great Pyramid of Giza"))
    }

    @Test
    fun `should throw NoLocationRetrieved when error happen after request the api`() = runTest {
        // Given
        val mockFunc: HttpRequestBuilder.() -> Unit = mockk(relaxed = true)
        coEvery { httpResponse.status } returns HttpStatusCode.NotFound
        coEvery { mockFunc.invoke(any()) } returns Unit
        coEvery { httpClient.get(any<String>(), mockFunc) } returns httpResponse
        coEvery { httpResponse.bodyAsText() } returns """
            {"latitude":29.9791854, "longitude":31.1316879, "label":"The Great Pyramid of Giza"}
        """.trimIndent()

        // When, Then
        assertThrows<NoLocationRetrieved> {
            locationFetcher.getLocation()
        }
    }

    @Test
    fun `should throw NoLocationRetrieved when response not correct`() = runTest {
        // Given
        val mockFunc: HttpRequestBuilder.() -> Unit = mockk(relaxed = true)
        coEvery { httpResponse.status } returns HttpStatusCode.OK
        coEvery { mockFunc.invoke(any()) } returns Unit
        coEvery { httpClient.get(any<String>(), mockFunc) } returns httpResponse
        coEvery { httpResponse.bodyAsText() } returns """
            <HTML><HEAD><META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=iso-8859-1"><TITLE>ERROR: The request could not be satisfied</TITLE></HEAD>
        """.trimIndent()

        // When, Then
        assertThrows<NoLocationRetrieved> {
            locationFetcher.getLocation()
        }
    }
}