package data.util.location_getter

import domain.models.Location
import domain.util.location_getter.LocationFetcher
import io.ktor.client.*

class CurrentLocationFetcher(private val httpClient: HttpClient) : LocationFetcher {
    override suspend fun getLocation(): Location {
        return Location(0.0, 0.0, "")
    }
}