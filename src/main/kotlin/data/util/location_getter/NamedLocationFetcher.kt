package data.util.location_getter

import domain.models.Location
import domain.util.location_getter.LocationFetcher
import io.ktor.client.*

const val POSITION_STACK_URL = "https://positionstack.com/geo_api.php?query=%s"

class NamedLocationFetcher(private val placeName: String, private val httpClient: HttpClient) : LocationFetcher {
    override suspend fun getLocation(): Location {
        return Location(0.0, 0.0, "")
    }
}