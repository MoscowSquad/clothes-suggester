package domain.repository

import domain.models.Location

interface LocationRepository {
    suspend fun getLocation(city: String): Location
}