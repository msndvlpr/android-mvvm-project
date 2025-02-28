package de.kleinanzeigen.challenge.data.repository.realestate

import de.kleinanzeigen.challenge.data.datasource.remote.NetworkDataSource
import de.kleinanzeigen.challenge.data.repositpry.realestate.DefaultRealEstateRepository
import de.kleinanzeigen.challenge.data.repositpry.realestate.model.Address
import de.kleinanzeigen.challenge.data.repositpry.realestate.model.AdsDataDetails
import de.kleinanzeigen.challenge.data.repositpry.realestate.model.Attribute
import de.kleinanzeigen.challenge.data.repositpry.realestate.model.Document
import de.kleinanzeigen.challenge.data.repositpry.realestate.model.Price
import de.kleinanzeigen.challenge.util.constants.PREDEFINED_ADS_ID
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultRealEstateRepositoryTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
    }


    @Test
    fun `getSingleAdsDetails emits success result when network call is successful`() = runTest {
        val repository = createTestRepository()

        val adId = PREDEFINED_ADS_ID
        repository.getSingleAdsDetails(adId)
        testDispatcher.scheduler.advanceUntilIdle()
        val expectedResult = createAdsDataDetailsResponse()

        val emittedResult = repository.adsDetailsFlow.first()
        assertTrue(emittedResult?.isSuccess == true)
        assertEquals(expectedResult, (emittedResult?.getOrNull() as AdsDataDetails))
    }

    @Test
    fun `getSingleAdsDetails emits failure result when network call fails`() = runTest {
        val repository = createTestRepository(Error.THROW_EXCEPTION)

        val adId = PREDEFINED_ADS_ID
        repository.getSingleAdsDetails(adId)
        testDispatcher.scheduler.advanceUntilIdle()

        val emittedResult = repository.adsDetailsFlow.first()
        assertTrue(emittedResult?.isFailure == true)
    }

    private fun createTestRepository(
        error: Error = Error.NONE,
        networkMock: NetworkDataSource = createNetworkDataSourceMock(error)
    ) = DefaultRealEstateRepository(networkMock)

    private fun createNetworkDataSourceMock(
        error: Error = Error.NONE,
        dealerData: AdsDataDetails = createAdsDataDetailsResponse()
    ) = mockk<NetworkDataSource> {
        if(error == Error.THROW_EXCEPTION){
            coEvery { getAdsDetailsById(any(), any(), any()) } returns Result.failure(Exception())

        } else {
            coEvery { getAdsDetailsById(any(), any(), any()) } returns Result.success(dealerData)
        }
    }

    private fun createAdsDataDetailsResponse(): AdsDataDetails {
        return AdsDataDetails(
            id = "1118635128",
            title = "Geräumige Dachgeschoss-Wohnung mit großer Sonnenterasse",
            price = Price(
                currencyCode = "EUR",
                amount = 859976
            ),
            visits = 2869,
            address = Address(
                street = "Weserstraße 15",
                city = "Berlin",
                zipCode = "12047",
                longitude = "13.4285519",
                latitude = "52.4874554"
            ),
            postedDateTime = "2021-10-08T08:01:00.000+0100",
            description = "Objektbeschreibung\nDas Gebäude\n\nDas zeitlos-schlichte Gebäude mit seinen hohen Decken, einladenden Dielenböden ...",
            attributes = listOf(
                Attribute(label = "Wohnfläche", value = "106,68", unit = "m²"),
                Attribute(label = "Zimmer", value = "3", unit = ""),
                Attribute(label = "Etage", value = "5", unit = ""),
                Attribute(label = "Wohnungstyp", value = "Dachgeschosswohnung"),
                Attribute(label = "Baujahr", value = "1900"),
                Attribute(label = "Provision", value = "Keine zusätzliche Käuferprovision", unit = "")
            ),
            features = listOf("Balkon", "Garage / Stellplatz", "Dusche", "Aufzug", "Keller"),
            pictures = listOf(
                "https://gateway.kleinanzeigen.de/coding-challenge/img/9NEAAOSw6xNhSgCC_{imageId}.jpeg",
                "https://gateway.kleinanzeigen.de/coding-challenge/img/21YAAOSwYEFhSgCC_{imageId}.jpeg",
                "https://gateway.kleinanzeigen.de/coding-challenge/img/aYUAAOSw919iDoMK_{imageId}.jpeg",
                "https://gateway.kleinanzeigen.de/coding-challenge/img/xCoAAOSwjZJhSgCC_{imageId}.jpeg",
                "https://gateway.kleinanzeigen.de/coding-challenge/img/eRQAAOSwUiBiLvGQ_{imageId}.jpeg",
                "https://gateway.kleinanzeigen.de/coding-challenge/img/FoQAAOSw9bVhSgCB_{imageId}.jpeg",
                "https://gateway.kleinanzeigen.de/coding-challenge/img/IhEAAOSwdF5hSgCC_{imageId}.jpeg",
                "https://gateway.kleinanzeigen.de/coding-challenge/img/LukAAOSwZEFhSgCC_{imageId}.jpeg",
                "https://gateway.kleinanzeigen.de/coding-challenge/img/1rQAAOSwbDViLvGT_{imageId}.jpeg",
                "https://gateway.kleinanzeigen.de/coding-challenge/img/w4sAAOSwYRJhSgCD_{imageId}.jpeg",
                "https://gateway.kleinanzeigen.de/coding-challenge/img/zMcAAOSwQaNhSgCC_{imageId}.jpeg"
            ),
            documents = listOf(
                Document(
                    link = "https://gateway.kleinanzeigen.de/coding-challenge/doc/e89b8999-0d1f-4447-9da9-01a0f7d8dfba.pdf",
                    title = "Grundriss"
                ),
                Document(
                    link = "https://gateway.kleinanzeigen.de/coding-challenge/doc/2972ace9-271f-4b7c-b55b-4d3e4d6538bd.pdf",
                    title = "Energieausweis"
                )
            )
        )

    }

    enum class Error {
        NONE,
        THROW_EXCEPTION
    }

}