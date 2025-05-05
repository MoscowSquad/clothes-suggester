package domain.util.location_getter

import domain.models.Location

interface LocationFetcher {
    fun getLocation(): Location
}