package domain.use_cases

import domain.models.Location
import domain.repository.LocationRepository

class GetLocationUseCase(private val repository: LocationRepository) {
    suspend fun execute(): Location {
        return repository.getCurrentLocation()
    }
}