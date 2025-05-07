package data.repository

import domain.models.Location
import domain.repository.LocationRepository

class LocationRepositoryImpl : LocationRepository {
    override suspend fun getCurrentLocation(): Location {
        return Location(
            latitude = 52.52,
            longitude = 13.41,
            label = "Berlin"
        )
    }
}
