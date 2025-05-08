package data.util.location_getter

import domain.models.Location
import domain.models.NoLocationRetrieved
import domain.util.location_getter.LocationFetcher
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

const val IP_API_URL = "http://ip-api.com/json"

class CurrentLocationFetcher(private val httpClient: HttpClient) : LocationFetcher {
    override suspend fun getLocation(): Location {
        val httpResponse = httpClient.get(IP_API_URL)
        if (httpResponse.status != HttpStatusCode.OK)
            throw NoLocationRetrieved()

        try {
            val response = httpResponse.bodyAsText()
            val json = Json {
                ignoreUnknownKeys = true
            }
            return json.decodeFromString<Location>(response)
        } catch (e: Exception) {
            throw NoLocationRetrieved()
        }
    }
}