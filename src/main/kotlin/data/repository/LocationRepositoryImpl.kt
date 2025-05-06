package data.repository

import domain.models.Location
import domain.repository.LocationRepository
import domain.util.location_getter.LocationFetcher

class LocationRepositoryImpl(private val locationFetcher: LocationFetcher) : LocationRepository {
    override suspend fun getLocation(): Location {
        return locationFetcher.getLocation()
    }
}