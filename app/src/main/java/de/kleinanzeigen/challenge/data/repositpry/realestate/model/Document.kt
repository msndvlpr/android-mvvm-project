package de.kleinanzeigen.challenge.data.repositpry.realestate.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Document(

    @SerialName("link")
    var link: String? = null,

    @SerialName("title")
    var title: String? = null

)