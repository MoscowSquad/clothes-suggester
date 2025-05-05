package domain.location_getter

import domain.models.Location
import domain.util.location_getter.LocationFetcher

class NamedLocationFetcher(placeName: String) : LocationFetcher {
    override fun getLocation(): Location {
        TODO("Not yet implemented")
    }
}