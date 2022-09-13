package io.aretemed.drakkar.client

import io.aretemed.drakkar.config.DrakkarWebClientProperties
import io.aretemed.drakkar.model.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.*
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.netty.http.client.HttpClient
import java.time.Duration

/**
 * An Entry Point Component for calling the Drakkar API
 */
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

    /**
     * A method for obtaining a Holder of Room API
     */
    fun roomAPI() = RoomAPI()

    /**
     * Just a Holder of Room API
     */
    inner class RoomAPI {

        /**
         * Getting available Rooms
         *
         * @param limit The Limit for result of available Rooms
         * @param offset The offset for result of available Rooms
         * @return Rooms object which holds info about available "Rooms" @see io.aretemed.drakkar.model.Rooms
         */
        fun rooms(limit: Int? = 0, offset: Int? = 0): Rooms {
            val queryParams = LinkedMultiValueMap<String, String>()
            if (limit != null && limit > 0) {
                queryParams["limit"] = limit.toString()
            }
            if (offset != null && offset > 0) {
                queryParams["offset"] = offset.toString()
            }

            var clientResponse: ClientResponse? = null
            val responseBody = webClient()
                .get()
                .uri{uriBuilder -> uriBuilder.path("api/rooms/").queryParams(queryParams).build()}
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

        /**
         * Getting a Room by id
         *
         * @param id The id of the desired Room
         * @return Room object representing the Room Entity itself @see io.aretemed.drakkar.model.Room
         */
        fun room(id: String): Room {
            var clientResponse: ClientResponse? = null
            val responseBody = webClient()
                .get()
                .uri("api/rooms/${id}/")
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

        /**
         * Creating a Room
         *
         * @param room The desired Room to be created
         * @return CreateRoomStatus object representing the status of creating the Room @see io.aretemed.drakkar.model.CreateRoomStatus
         */
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

        /**
         * Updating a Room
         *
         * @param room The desired Room to be updated to the passed state
         * @return Room object after update @see io.aretemed.drakkar.model.Room
         */
        fun updateRoom(room: Room): Room {
            var clientResponse: ClientResponse? = null
            val responseBody = webClient()
                .put()
                .uri("api/rooms/${room.id}/update-room/")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(mapToRoomForUpdate(room)), RoomForUpdate::class.java)
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

        private fun mapToRoomForUpdate(room: Room): RoomForUpdate {
            return RoomForUpdate(
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

        /**
         * Create Meeting Token
         *
         * @param createMeetingTokenInfo a holder of params to Create Meeting Token
         * @return CreateMeetingTokenStatus object representing the status of creating the Meeting Token @see io.aretemed.drakkar.model.CreateMeetingTokenStatus
         */
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

    /**
     * A method for obtaining a Holder of Encounter API
     */
    fun encounterAPI() = EncounterAPI()

    /**
     * Just a Holder of Room API
     */
    inner class EncounterAPI {

        /**
         * Getting available Encounters
         *
         * @param limit The Limit for result of available Encounters
         * @param offset The offset for result of available Encounters
         * @return Rooms object which holds info about available "Encounters" @see io.aretemed.drakkar.model.Encounters
         */
        fun encounters(limit: Int? = 0, offset: Int? = 0): Encounters {
            val queryParams = LinkedMultiValueMap<String, String>()
            if (limit != null && limit > 0) {
                queryParams["limit"] = limit.toString()
            }
            if (offset != null && offset > 0) {
                queryParams["offset"] = offset.toString()
            }

            var clientResponse: ClientResponse? = null
            val responseBody = webClient()
                .get()
                .uri{uriBuilder -> uriBuilder.path("api/encounters/").queryParams(queryParams).build()}
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

        /**
         * Getting an Encounter by id
         *
         * @param id The id of the desired Encounter
         * @return Encounter object representing the Encounter Entity itself @see io.aretemed.drakkar.model.Encounter
         */
        fun encounter(id: String): Encounter {
            var clientResponse: ClientResponse? = null
            val responseBody = webClient()
                .get()
                .uri("api/encounters/${id}/")
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