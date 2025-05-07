package domain.models

import io.ktor.utils.io.errors.*

class NoLocationRetrieved(message: String = "No location retrieved") : IOException(message)

class ErrorOnRetrieveLocation(message: String) : IOException(message)