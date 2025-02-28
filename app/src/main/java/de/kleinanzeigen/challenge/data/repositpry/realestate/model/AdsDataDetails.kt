package de.kleinanzeigen.challenge.data.repositpry.realestate.model

import de.kleinanzeigen.challenge.util.constants.PREDEFINED_IMAGE_ID
import de.kleinanzeigen.challenge.util.constants.PREDEFINED_IMAGE_ID_HIGH_RES
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AdsDataDetails(

    @SerialName("id")
    var id: String? = null,

    @SerialName("title")
    var title: String? = null,

    @SerialName("price")
    var price: Price? = Price(),

    @SerialName("visits")
    var visits: Int? = null,

    @SerialName("address")
    var address: Address? = Address(),

    @SerialName("posted-date-time")
    var postedDateTime: String? = null,

    @SerialName("description")
    var description: String? = null,

    @SerialName("attributes")
    var attributes: List<Attribute>? = listOf(),

    @SerialName("features")
    var features: List<String>? = listOf(),

    @SerialName("pictures")
    var pictures: List<String>? = listOf(),

    @SerialName("documents")
    var documents: List<Document>? = listOf()
){

    fun getLowResolutionPictures(): List<String>? {
        return pictures?.map { it.replace("{imageId}", PREDEFINED_IMAGE_ID) }
    }

    fun getHighResolutionPictures(): List<String>? {
        return pictures?.map { it.replace("{imageId}", PREDEFINED_IMAGE_ID_HIGH_RES) }
    }
}