package io.aretemed.drakkarsdk.client

import com.example.webclientconsumerkotlinsample.model.Rooms
import com.google.gson.Gson
import io.aretemed.drakkar.client.DrakkarWebClient
import io.aretemed.drakkar.config.DrakkarSDKWebClientProperties
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import java.io.IOException
import kotlin.test.assertEquals


@ExtendWith(MockitoExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DrakkarWebClientTest {

    var mockServer: MockWebServer = MockWebServer()
    var webClient = DrakkarWebClient()

    @BeforeAll
    @Throws(IOException::class)
    fun setUp() {
        val testUrl = "http://localhost"
        mockServer.start()
        mockServer.url(testUrl)
        webClient.properties = DrakkarSDKWebClientProperties(
            baseUrl = "${testUrl}:${mockServer.port}/",
            token = "TestToken",
            responseTimeout = 60L
        )
    }

    @AfterAll
    @Throws(IOException::class)
    fun tearDown() {
        mockServer.shutdown()
    }

    @Test
    fun rooms() {
        val roomsMock = Rooms(count = 50, next = "next", previous = "previous", results = emptyList())
        mockServer.enqueue(
            MockResponse()
                .setBody(Gson().toJson(roomsMock))
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        )
        val roomsFromResponse = webClient.rooms()
        val recordedRequest = mockServer.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/api/rooms/", recordedRequest.path)
        assertEquals(roomsMock, roomsFromResponse)
    }
}