package domain.use_cases

import domain.models.Location
import domain.repository.LocationRepository

class GetLocationUseCase(private val repository: LocationRepository) {
    suspend fun getLocation(): Location {
        return repository.getCurrentLocation()
    }
}