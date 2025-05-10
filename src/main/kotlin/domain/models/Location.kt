package domain.models

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class Location constructor(
    @SerialName("latitude")
    @JsonNames("lat")
    val latitude: Double,

    @SerialName("longitude")
    @JsonNames("lon")
    val longitude: Double,

    @SerialName("label")
    @JsonNames("city")
    val label: String,
)
