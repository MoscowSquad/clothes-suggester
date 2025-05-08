package data.repository

import domain.models.Location
import domain.repository.LocationRepository
import domain.util.location_getter.LocationFetcher

class LocationRepositoryImpl() : LocationRepository {
    override suspend fun getLocation(locationFetcher: LocationFetcher): Location {
        return locationFetcher.getLocation()
    }
}