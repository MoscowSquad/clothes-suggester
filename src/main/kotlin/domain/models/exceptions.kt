package domain.models

import io.ktor.utils.io.errors.*

class NoLocationRetrieved(message:String = "No location retrieved") : IOException()