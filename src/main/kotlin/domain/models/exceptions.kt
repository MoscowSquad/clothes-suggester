package domain.models

import io.ktor.http.*
import io.ktor.utils.io.errors.*

class NoLocationRetrieved : IOException("No location retrieved")

class FailedFetchWeatherDataException(status: HttpStatusCode) : Exception("Can't retrieve data from API, response statue: $status")