package domain.use_cases

import domain.models.Location
import domain.util.location_getter.LocationFetcher

class GetLocationUseCase {
    suspend fun getLocation(locationGetter: LocationFetcher): Location {
        return locationGetter.getLocation()
    }
}