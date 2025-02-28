package de.kleinanzeigen.challenge.data.repositpry.realestate.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Item(

    @SerialName("adId")
    var adId: String? = null,

    @SerialName("title")
    var title: String? = null,

    @SerialName("image")
    var image: String? = null,

    @SerialName("priceAmount")
    var priceAmount: Int? = null

)