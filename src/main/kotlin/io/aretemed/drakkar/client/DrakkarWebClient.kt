package io.aretemed.drakkar.client

import com.example.webclientconsumerkotlinsample.model.*
import io.aretemed.drakkar.config.DrakkarWebClientProperties
import io.aretemed.drakkar.model.Encounter
import io.aretemed.drakkar.model.Encounters
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.stereotype.Component
import org.springframework.web.client.*
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.netty.http.client.HttpClient
import java.time.Duration

@Component
class DrakkarWebClient {

    @Autowired
    var properties: DrakkarWebClientProperties? = null

    private fun webClient(): WebClient {
        val client = HttpClient.create()
            .responseTimeout(Duration.ofSeconds(properties?.responseTimeout ?: 60L))
        return WebClient.builder()
            .baseUrl(properties?.baseUrl ?: "https://dev-drakkar.aretemed.io/")
            .defaultHeaders { headers ->
                headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                headers.set(HttpHeaders.AUTHORIZATION, "Token ${properties?.token}")
            }
            .clientConnector(ReactorClientHttpConnector(client))
            .build()
    }

    fun roomAPI() = RoomAPI()
    inner class RoomAPI {

        fun rooms(limit: Int? = 0, offset: Int? = 0): Rooms {
            val queryParams = mutableMapOf<String, Any>()
            if (limit != null && limit > 0) {
                queryParams["limit"] = limit
            }
            if (offset != null && offset > 0) {
                queryParams["offset"] = offset
            }

            var clientResponse: ClientResponse? = null
            val responseBody = webClient()
                .get()
                .uri("api/rooms/", queryParams)
                .exchangeToMono { response ->
                    clientResponse = response
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return@exchangeToMono response.bodyToMono(Rooms::class.java)
                    } else {
                        return@exchangeToMono response.bodyToMono(String::class.java)
                    }
                }
                .block()

            if (responseBody is Rooms) {
                return responseBody
            } else {
                throw RestClientException(
                    responseBody!!.toString(),
                    clientResponse?.createException()?.block() as Throwable
                )
            }
        }

        fun room(id: String): Room {
            var clientResponse: ClientResponse? = null
            val responseBody = webClient()
                .get()
                .uri("api/rooms/${id}")
                .exchangeToMono { response ->
                    clientResponse = response
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return@exchangeToMono response.bodyToMono(Room::class.java)
                    } else {
                        return@exchangeToMono response.bodyToMono(String::class.java)
                    }
                }
                .block()

            if (responseBody is Room) {
                return responseBody
            } else {
                throw RestClientException(
                    responseBody!!.toString(),
                    clientResponse?.createException()?.block() as Throwable
                )
            }
        }

        fun createRoom(room: Room): CreateRoomStatus {
            var clientResponse: ClientResponse? = null
            val responseBody = webClient()
                .post()
                .uri("api/rooms/create-room/")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(room), Room::class.java)
                .exchangeToMono { response ->
                    clientResponse = response
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return@exchangeToMono response.bodyToMono(CreateRoomStatus::class.java)
                    } else {
                        return@exchangeToMono response.bodyToMono(String::class.java)
                    }

                }
                .block()

            if (responseBody is CreateRoomStatus) {
                return responseBody
            } else {
                throw RestClientException(
                    responseBody!!.toString(),
                    clientResponse?.createException()?.block() as Throwable
                )
            }
        }

        fun updateRoom(room: Room): Room {
            var clientResponse: ClientResponse? = null
            val responseBody = webClient()
                .put()
                .uri("api/rooms/${room.id}/update-room/")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(cleanReadOnlyRoomFields(room)), Room::class.java)
                .exchangeToMono { response ->
                    clientResponse = response
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return@exchangeToMono response.bodyToMono(Room::class.java)
                    } else {
                        return@exchangeToMono response.bodyToMono(String::class.java)
                    }
                }
                .block()

            if (responseBody is Room) {
                return responseBody
            } else {
                throw RestClientException(
                    responseBody!!.toString(),
                    clientResponse?.createException()?.block() as Throwable
                )
            }
        }

        private fun cleanReadOnlyRoomFields(room: Room): Room {
            return Room(
                notBeforeDateTime = room.notBeforeDateTime,
                expirationDateTime = room.expirationDateTime,
                enableChat = room.enableChat,
                enableKnocking = room.enableKnocking,
                enablePeopleUI = room.enablePeopleUI,
                enablePrejoinUI = room.enablePrejoinUI,
                enableScreenshare = room.enableScreenshare,
                meetingJoinHook = room.meetingJoinHook
            )
        }

        fun createMeetingToken(createMeetingTokenInfo: CreateMeetingTokenInfo): CreateMeetingTokenStatus {
            var clientResponse: ClientResponse? = null
            val responseBody = webClient()
                .post()
                .uri("api/rooms/${createMeetingTokenInfo.roomId}/create-meeting-token/")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(createMeetingTokenInfo), CreateMeetingTokenInfo::class.java)
                .exchangeToMono { response ->
                    clientResponse = response
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return@exchangeToMono response.bodyToMono(CreateMeetingTokenStatus::class.java)
                    } else {
                        return@exchangeToMono response.bodyToMono(String::class.java)
                    }

                }
                .block()

            if (responseBody is CreateMeetingTokenStatus) {
                return responseBody
            } else {
                throw RestClientException(
                    responseBody!!.toString(),
                    clientResponse?.createException()?.block() as Throwable
                )
            }
        }
    }

    fun encounterAPI() = EncounterAPI()
    inner class EncounterAPI {
        fun encounters(limit: Int? = 0, offset: Int? = 0): Encounters {
            val queryParams = mutableMapOf<String, Any>()
            if (limit != null && limit > 0) {
                queryParams["limit"] = limit
            }
            if (offset != null && offset > 0) {
                queryParams["offset"] = offset
            }

            var clientResponse: ClientResponse? = null
            val responseBody = webClient()
                .get()
                .uri("api/encounters/", queryParams)
                .exchangeToMono { response ->
                    clientResponse = response
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return@exchangeToMono response.bodyToMono(Encounters::class.java)
                    } else {
                        return@exchangeToMono response.bodyToMono(String::class.java)
                    }
                }
                .block()

            if (responseBody is Encounters) {
                return responseBody
            } else {
                throw RestClientException(
                    responseBody!!.toString(),
                    clientResponse?.createException()?.block() as Throwable
                )
            }
        }
        fun encounter(id: String): Encounter {
            var clientResponse: ClientResponse? = null
            val responseBody = webClient()
                .get()
                .uri("api/encounters/${id}")
                .exchangeToMono { response ->
                    clientResponse = response
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return@exchangeToMono response.bodyToMono(Encounter::class.java)
                    } else {
                        return@exchangeToMono response.bodyToMono(String::class.java)
                    }
                }
                .block()

            if (responseBody is Encounter) {
                return responseBody
            } else {
                throw RestClientException(
                    responseBody!!.toString(),
                    clientResponse?.createException()?.block() as Throwable
                )
            }
        }
    }

}