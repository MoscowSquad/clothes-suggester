package data.util

import kotlinx.serialization.json.Json

inline fun <reified T : Any> String.parseResponse(): T {
    val json = Json {
        ignoreUnknownKeys = true
    }
    return json.decodeFromString<T>(this)
}