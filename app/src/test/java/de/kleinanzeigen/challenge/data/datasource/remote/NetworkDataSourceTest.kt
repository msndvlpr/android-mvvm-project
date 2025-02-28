package de.kleinanzeigen.challenge.data.datasource.remote

import de.kleinanzeigen.challenge.data.repositpry.realestate.model.AdsDataDetails
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertNotNull

@OptIn(ExperimentalCoroutinesApi::class)
class NetworkDataSourceTest {

    private lateinit var networkDataSource: NetworkDataSource
    private val fakeId = "12345"
    private val fakeUsername = "user"
    private val fakePassword = "pass"
    private val fakeAdsDataDetails = AdsDataDetails(
        id = fakeId,
        title = "Sample Ad",
        description = "This is a sample ad.",
        pictures = emptyList()
    )

    @Before
    fun setUp() {
        val mockEngine = MockEngine { request ->
            // Return a response with the expected JSON structure
            respond(
                content = """{
                    "id": "$fakeId",
                    "title": "Sample Ad",
                    "description": "This is a sample ad.",
                    "pictures": []
                }""",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val httpClient = HttpClient(mockEngine)
        networkDataSource = NetworkDataSource { httpClient }
    }


    //todo: the test is failing unfortunately and could not be fixed in the give time
    @Test
    fun `getAdsDetailsById returns AdsDataDetails on success`() = runTest {
        val result = networkDataSource.getAdsDetailsById(fakeId, fakeUsername, fakePassword)

        assert(result.isSuccess) {
            "Expected successful result, but got failure: ${result.exceptionOrNull()}"
        }

        val data = result.getOrNull()
        assertNotNull(data) {
            "Expected AdsDataDetails to be returned, but it was null"
        }

        assert(data == fakeAdsDataDetails) {
            "Expected $fakeAdsDataDetails, but got $data"
        }
    }


}
