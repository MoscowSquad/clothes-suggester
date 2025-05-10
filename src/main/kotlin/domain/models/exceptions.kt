package domain.models

import io.ktor.http.*

class NoLocationRetrieved : Exception("No location retrieved")
class FailedFetchWeatherDataException(status: HttpStatusCode) :
    Exception("Can't retrieve data from API, response statue: $status")