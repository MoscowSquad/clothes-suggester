package data.util.location_getter

import domain.models.Location
import domain.models.NoLocationRetrieved
import domain.util.location_getter.LocationFetcher
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import org.json.JSONObject

const val POSITION_STACK_URL = "https://positionstack.com/geo_api.php?query=%s"

class NamedLocationFetcher(private val placeName: String, private val httpClient: HttpClient) : LocationFetcher {
    override suspend fun getLocation(): Location {
        val httpResponse = httpClient.get(getPlaceUrl())
        if (httpResponse.status != HttpStatusCode.OK)
            throw NoLocationRetrieved()

        try {
            val response = JSONObject(httpResponse.bodyAsText()).getJSONArray("data")[0].toString()
            val json = Json {
                ignoreUnknownKeys = true
            }
            return json.decodeFromString<Location>(response)
        } catch (e: Exception) {
            throw NoLocationRetrieved()
        }
    }

    private fun getPlaceUrl(): String {
        val formatedUrl = placeName.replace(" ", "%20")
        return POSITION_STACK_URL.format(formatedUrl)
    }
}