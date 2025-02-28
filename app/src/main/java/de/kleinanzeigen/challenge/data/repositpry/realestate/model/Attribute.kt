package de.kleinanzeigen.challenge.data.repositpry.realestate.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Attribute(

    @SerialName("label")
    var label: String? = null,

    @SerialName("value")
    var value: String? = null,

    @SerialName("unit")
    var unit: String? = null

)