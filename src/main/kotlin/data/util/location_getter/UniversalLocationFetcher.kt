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

class UniversalLocationFetcher(
    internal val strategy: LocationStrategy,
    private val httpClient: HttpClient
) : LocationFetcher {

    override suspend fun getLocation(): Location {
        return strategy.fetchLocation(httpClient)
    }

    sealed interface LocationStrategy {
        suspend fun fetchLocation(httpClient: HttpClient): Location
    }

    class CurrentLocationStrategy : LocationStrategy {
        companion object {
            const val IP_API_URL = "http://ip-api.com/json"
        }

        override suspend fun fetchLocation(httpClient: HttpClient): Location {
            val httpResponse = httpClient.get(IP_API_URL)
            if (httpResponse.status != HttpStatusCode.OK)
                throw NoLocationRetrieved()

            try {
                val response = httpResponse.bodyAsText()
                val json = Json { ignoreUnknownKeys = true }
                return json.decodeFromString<Location>(response)
            } catch (e: Exception) {
                throw NoLocationRetrieved()
            }
        }
    }

    class NamedLocationStrategy(internal val placeName: String) : LocationStrategy {
        companion object {
            const val POSITION_STACK_URL = "https://positionstack.com/geo_api.php?query=%s"
        }

        override suspend fun fetchLocation(httpClient: HttpClient): Location {
            val httpResponse = httpClient.get(getPlaceUrl())
            if (httpResponse.status != HttpStatusCode.OK)
                throw NoLocationRetrieved()

            try {
                val response = JSONObject(httpResponse.bodyAsText()).getJSONArray("data")[0].toString()
                val json = Json { ignoreUnknownKeys = true }
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
}