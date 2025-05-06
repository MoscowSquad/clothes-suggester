package domain.util.location_getter

import domain.models.Location

interface LocationFetcher {
    suspend fun getLocation(): Location
}