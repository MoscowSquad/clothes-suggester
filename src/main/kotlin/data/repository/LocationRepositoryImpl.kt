package data.repository

import domain.models.Location
import domain.repository.LocationRepository

class LocationRepositoryImpl : LocationRepository {
    override suspend fun getLocation(city: String): Location {
        TODO("Not yet implemented")
    }
}