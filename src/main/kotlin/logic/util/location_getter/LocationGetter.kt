package logic.util.location_getter

import logic.models.Location

interface LocationGetter {
    fun getLocation(): Location
}