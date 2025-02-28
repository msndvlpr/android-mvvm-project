package de.kleinanzeigen.challenge.data.repositpry.realestate.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Price(

    @SerialName("currency")
    var currencyCode: String? = null,

    @SerialName("amount")
    var amount: Int? = null

)