package domain.use_cases

import domain.models.Location
import domain.util.location_getter.LocationFetcher

class GetLocationUseCase(private val locationGetter: LocationFetcher) {
    suspend fun getLocation(): Location {
        return locationGetter.getLocation()
    }
}