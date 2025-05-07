package domain.models.exceptions

class FailedFetchWeatherDataException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause)