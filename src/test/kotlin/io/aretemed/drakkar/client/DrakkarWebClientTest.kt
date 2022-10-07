package io.aretemed.drakkar.client

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.aretemed.drakkar.config.DrakkarWebClientProperties
import io.aretemed.drakkar.model.*
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
import java.util.*
import kotlin.test.assertEquals


@ExtendWith(MockitoExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DrakkarWebClientTest {

    var mockServer: MockWebServer = MockWebServer()
    var webClient = DrakkarWebClient()
    val jsonMapper = jacksonObjectMapper()

    @BeforeAll
    @Throws(IOException::class)
    fun setUp() {
        val testUrl = "http://localhost"
        mockServer.start()
        mockServer.url(testUrl)
        webClient.properties = DrakkarWebClientProperties(
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
                .setBody(jsonMapper.writeValueAsString(roomsMock))
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        )

        val roomsFromResponse = webClient.roomAPI().rooms()
        val recordedRequest = mockServer.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/api/rooms/", recordedRequest.path)
        assertEquals(jsonMapper.writeValueAsString(roomsMock), jsonMapper.writeValueAsString(roomsFromResponse))
    }

    @Test
    fun roomsByLimitAndOffset() {
        val roomsMock = Rooms(count = 50, next = "next", previous = "previous", results = emptyList())

        mockServer.enqueue(
            MockResponse()
                .setBody(jsonMapper.writeValueAsString(roomsMock))
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        )

        val roomsFromResponse = webClient.roomAPI().rooms(20, 100)
        val recordedRequest = mockServer.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/api/rooms/?limit=20&offset=100", recordedRequest.path)
        assertEquals(jsonMapper.writeValueAsString(roomsMock), jsonMapper.writeValueAsString(roomsFromResponse))
    }

    @Test
    fun room() {
        val roomMock = Room(
            id = "12345",
            name = "TestRoom",
            business = "55555",
            notBeforeDateTime = Date(),
            expirationDateTime = Date()
        )

        mockServer.enqueue(
            MockResponse()
                .setBody(jsonMapper.writeValueAsString(roomMock))
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        )

        val roomFromResponse = webClient.roomAPI().room(roomMock.id!!)
        val recordedRequest = mockServer.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/api/rooms/${roomMock.id}/", recordedRequest.path)
        assertEquals(jsonMapper.writeValueAsString(roomMock), jsonMapper.writeValueAsString(roomFromResponse))
    }

    @Test
    fun createRoom() {
        val createRoomStatusMock =
            CreateRoomStatus(success = true, url = "https://localhost/embedded/ABCDEFG1234567/", id = "abcde12345")

        mockServer.enqueue(
            MockResponse()
                .setBody(jsonMapper.writeValueAsString(createRoomStatusMock))
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        )

        val createRoomStatusFromResponse = webClient.roomAPI().createRoom(Room())
        val recordedRequest = mockServer.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/api/rooms/create-room/", recordedRequest.path)
        assertEquals(jsonMapper.writeValueAsString(createRoomStatusMock), jsonMapper.writeValueAsString(createRoomStatusFromResponse))
    }

    @Test
    fun updateRoom() {

        val roomMock = Room(
            id = "12345",
            name = "TestRoom",
            business = "55555",
            notBeforeDateTime = Date(),
            expirationDateTime = Date()
        )

        mockServer.enqueue(
            MockResponse()
                .setBody(jsonMapper.writeValueAsString(roomMock))
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        )

        val roomFromResponse = webClient.roomAPI().updateRoom(roomMock)
        val recordedRequest = mockServer.takeRequest()
        assertEquals("PUT", recordedRequest.method)
        assertEquals("/api/rooms/${roomMock.id}/update-room/", recordedRequest.path)
        assertEquals(jsonMapper.writeValueAsString(roomMock), jsonMapper.writeValueAsString(roomFromResponse))
    }

    @Test
    fun createMeetingToken() {
        val createMeetingTokenInfo = CreateMeetingTokenInfo(roomId = "12345", userId = "56789", userName = "TestUser", isOwner = true)
        val createMeetingTokenStatusMock = CreateMeetingTokenStatus(success = true, url = "https://localhost/ABCDEFG1234567?t=abcde12345")

        mockServer.enqueue(
            MockResponse()
                .setBody(jsonMapper.writeValueAsString(createMeetingTokenStatusMock))
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        )

        val createMeetingTokenStatusFromResponse = webClient.roomAPI().createMeetingToken(createMeetingTokenInfo)
        val recordedRequest = mockServer.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/api/rooms/${createMeetingTokenInfo.roomId}/create-meeting-token/", recordedRequest.path)
        assertEquals(jsonMapper.writeValueAsString(createMeetingTokenStatusMock), jsonMapper.writeValueAsString(createMeetingTokenStatusFromResponse))
    }

    @Test
    fun encounters() {
        val encountersMock = Encounters(count = 50, next = "next", previous = "previous", results = emptyList())

        mockServer.enqueue(
            MockResponse()
                .setBody(jsonMapper.writeValueAsString(encountersMock))
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        )

        val encountersFromResponse = webClient.encounterAPI().encounters()
        val recordedRequest = mockServer.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/api/encounters/", recordedRequest.path)
        assertEquals(jsonMapper.writeValueAsString(encountersMock), jsonMapper.writeValueAsString(encountersFromResponse))
    }

    @Test
    fun encountersByLimitAndOffset() {
        val encountersMock = Encounters(count = 50, next = "next", previous = "previous", results = emptyList())

        mockServer.enqueue(
            MockResponse()
                .setBody(jsonMapper.writeValueAsString(encountersMock))
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        )

        val encountersFromResponse = webClient.encounterAPI().encounters(20, 100)
        val recordedRequest = mockServer.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/api/encounters/?limit=20&offset=100", recordedRequest.path)
        assertEquals(jsonMapper.writeValueAsString(encountersMock), jsonMapper.writeValueAsString(encountersFromResponse))
    }

    @Test
    fun encounter() {
        val encounterMock = Encounter(
            id = "12345",
            room = "1234567",
            scheduledStartDateTime = Date(),
            created = Date(),
            updated = Date()
        )

        mockServer.enqueue(
            MockResponse()
                .setBody(jsonMapper.writeValueAsString(encounterMock))
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        )

        val encounterFromResponse = webClient.encounterAPI().encounter(encounterMock.id!!)
        val recordedRequest = mockServer.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/api/encounters/${encounterMock.id}/", recordedRequest.path)
        assertEquals(jsonMapper.writeValueAsString(encounterMock), jsonMapper.writeValueAsString(encounterFromResponse))
    }

}