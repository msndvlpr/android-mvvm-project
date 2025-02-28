package de.kleinanzeigen.challenge.data.repositpry.realestate.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Address(

    @SerialName("street")
    var street: String? = null,

    @SerialName("city")
    var city: String? = null,

    @SerialName("zip-code")
    var zipCode: String? = null,

    @SerialName("longitude")
    var longitude: String? = null,

    @SerialName("latitude")
    var latitude: String? = null
)