package domain.use_cases

import domain.util.location_getter.LocationFetcher

class GetLocationUseCase(private val locationGetter: LocationFetcher) {
}