package domain.repository

import domain.models.Location
import domain.util.location_getter.LocationFetcher

interface LocationRepository {
    suspend fun getLocation(locationFetcher: LocationFetcher): Location
}