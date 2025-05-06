package data.util.location_getter

import domain.models.Location
import domain.util.location_getter.LocationFetcher

class NamedLocationFetcher(placeName: String) : LocationFetcher {
    override suspend fun getLocation(): Location {
        return Location(0.0, 0.0, "")
    }
}