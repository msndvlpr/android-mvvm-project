package de.kleinanzeigen.challenge.data.repositpry.realestate

import de.kleinanzeigen.challenge.data.repositpry.realestate.model.AdsItems
import de.kleinanzeigen.challenge.data.repositpry.realestate.model.AdsDataDetails
import kotlinx.coroutines.flow.StateFlow

interface RealEstateRepository {

    val allAdsFlow: StateFlow<Result<AdsItems?>?>

    val adsDetailsFlow: StateFlow<Result<AdsDataDetails?>?>

    suspend fun getAllAds()

    suspend fun getSingleAdsDetails(id: String)

}