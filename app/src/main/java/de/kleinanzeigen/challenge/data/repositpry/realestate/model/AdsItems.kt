package de.kleinanzeigen.challenge.data.repositpry.realestate.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AdsItems(

    @SerialName("items")
    var items: List<Item>? = listOf()

)