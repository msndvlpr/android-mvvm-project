package de.kleinanzeigen.challenge.ui.details

import de.kleinanzeigen.challenge.data.repositpry.realestate.RealEstateRepository
import de.kleinanzeigen.challenge.data.repositpry.realestate.model.Address
import de.kleinanzeigen.challenge.data.repositpry.realestate.model.AdsDataDetails
import de.kleinanzeigen.challenge.data.repositpry.realestate.model.Attribute
import de.kleinanzeigen.challenge.data.repositpry.realestate.model.Document
import de.kleinanzeigen.challenge.data.repositpry.realestate.model.Price
import de.kleinanzeigen.challenge.ui.ViewState
import de.kleinanzeigen.challenge.util.constants.PREDEFINED_ADS_ID
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test


class ItemDetailsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onInit sets viewState to Loading and triggers data fetch`() = runTest {
        val mockRepo = createRealEstateRepositoryMock()

        val viewModel = createTestViewModel(realEstateRepo = mockRepo)

        viewModel.onUIEvent(ItemDetailsViewModel.UiEvent.OnInit)

        assert(viewModel.viewState.value is ViewState.Loading)

        coVerify { mockRepo.getSingleAdsDetails(PREDEFINED_ADS_ID) }
    }

    @Test
    fun `successful data load updates viewState to Success`() = runTest {
        val adsDetails = createMockedAdsDetails()

        val viewModel = createTestViewModel()

        testDispatcher.scheduler.advanceUntilIdle()

        assert(viewModel.viewState.value is ViewState.Success)
        assert((viewModel.viewState.value as ViewState.Success).data == adsDetails)
    }

    @Test
    fun `failed data load updates viewState to Failure`() = runTest {
        val viewModel = createTestViewModel(Error.THROW_EXCEPTION)

        testDispatcher.scheduler.advanceUntilIdle()

        assert(viewModel.viewState.value is ViewState.Failure)
    }


    private fun createTestViewModel(
        error: Error = Error.NONE,
        realEstateRepo: RealEstateRepository = createRealEstateRepositoryMock(error),
    ) = ItemDetailsViewModel(realEstateRepo)

    private fun createRealEstateRepositoryMock(
        error: Error = Error.NONE
    ) = mockk<RealEstateRepository> {

        if (error == Error.THROW_EXCEPTION) {
            coEvery { adsDetailsFlow } returns MutableStateFlow(Result.failure(Exception()))
            coEvery { getSingleAdsDetails(any()) } throws Exception()

        } else {
            coEvery { adsDetailsFlow } returns MutableStateFlow(Result.success(createMockedAdsDetails()))
            coEvery { getSingleAdsDetails(any()) } just runs
        }
    }

    private fun createMockedAdsDetails(): AdsDataDetails {
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


