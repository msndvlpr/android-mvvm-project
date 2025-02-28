package de.kleinanzeigen.challenge.data.datasource.remote

import de.kleinanzeigen.challenge.data.repositpry.realestate.model.AdsDataDetails
import de.kleinanzeigen.challenge.data.repositpry.realestate.model.AdsItems
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.basicAuth
import io.ktor.client.request.get
import io.ktor.http.HttpHeaders
import io.ktor.http.headers
import timber.log.Timber

internal class NetworkDataSource(private val clientProvider: suspend () -> HttpClient) {

    suspend fun getAdsDetailsById(id: String, username: String, password: String): Result<AdsDataDetails> =
        runCatching<NetworkDataSource, AdsDataDetails> {

            val response = clientProvider().get("$GET_REAL_ESTATE_ADS_URL${id}") {
                headers {
                    append(HttpHeaders.ContentType, "application/json")
                }
                basicAuth(username = username, password = password)
            }
            response.body()

        }.onFailure { error ->
            Timber.e(error.message)
        }

    //todo: to be used later for project feature extension
    suspend fun getAllAds(username: String, password: String): Result<AdsItems> =
        runCatching<NetworkDataSource, AdsItems> {

            val response = clientProvider().get(GET_REAL_ESTATE_ADS_URL) {
                headers {
                    append(HttpHeaders.ContentType, "application/json")
                }
                basicAuth(username = username, password = password)
            }
            response.body()

        }.onFailure { error ->
            Timber.e(error.message)
        }
}
