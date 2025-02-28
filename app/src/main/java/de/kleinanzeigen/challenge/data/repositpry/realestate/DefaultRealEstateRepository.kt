package de.kleinanzeigen.challenge.data.repositpry.realestate

import de.kleinanzeigen.challenge.data.datasource.remote.NetworkDataSource
import de.kleinanzeigen.challenge.data.repositpry.realestate.model.AdsItems
import de.kleinanzeigen.challenge.data.repositpry.realestate.model.AdsDataDetails
import de.kleinanzeigen.challenge.util.constants.AUTHENTICATION_PASSWORD
import de.kleinanzeigen.challenge.util.constants.AUTHENTICATION_USERNAME
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import javax.inject.Inject

internal class DefaultRealEstateRepository @Inject constructor(
    private val networkDataSource: NetworkDataSource,

    ) : RealEstateRepository {

    private val _adsDetailsFlow = MutableStateFlow<Result<AdsDataDetails>?>(null)
    override val adsDetailsFlow: StateFlow<Result<AdsDataDetails>?> = _adsDetailsFlow

    private val _allAdsFlow = MutableStateFlow<Result<AdsItems>?>(null)
    override val allAdsFlow: StateFlow<Result<AdsItems>?> = _allAdsFlow


    override suspend fun getSingleAdsDetails(id: String) {
        var adsDetails: Result<AdsDataDetails>? = networkDataSource.getAdsDetailsById(
            id,
            username = AUTHENTICATION_USERNAME,
            password = AUTHENTICATION_PASSWORD
        )

        adsDetails?.onSuccess {
            adsDetails = Result.success(it)

        }?.onFailure {
            Timber.e(it.message)
            adsDetails = Result.failure(it)
        }

        _adsDetailsFlow.emit(adsDetails)
    }

    //todo: to be used later for project feature extension
    override suspend fun getAllAds() {
        var allAds: Result<AdsItems>? = networkDataSource.getAllAds(
            username = AUTHENTICATION_USERNAME,
            password = AUTHENTICATION_PASSWORD
        )

        allAds?.onSuccess {
            allAds = Result.success(it)

        }?.onFailure {
            Timber.e(it.message)
            allAds = Result.failure(it)
        }

        _allAdsFlow.emit(allAds)
    }


}