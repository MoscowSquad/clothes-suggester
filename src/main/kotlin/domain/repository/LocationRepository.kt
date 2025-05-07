package domain.repository

import domain.models.Location

interface LocationRepository {
    suspend fun getCurrentLocation(): Location
}
