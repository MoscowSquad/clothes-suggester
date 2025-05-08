package domain.models

import io.ktor.utils.io.errors.*

class NoLocationRetrieved(message: String = "No location retrieved") : IOException(message)

class FailedFetchWeatherDataException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause)