package io.aretemed.drakkar.client

import io.aretemed.drakkar.config.DrakkarWebClientProperties
import io.aretemed.drakkar.custom.Generated
import io.aretemed.drakkar.model.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.netty.http.client.HttpClient

/**
 * An Entry Point Component for calling the Drakkar API
 */
@Component
class DrakkarWebClient {

    @Autowired
    var properties: DrakkarWebClientProperties? = null

    @Generated
    private fun webClient(): WebClient {
        val baseUrl = properties?.baseUrl ?: "https://dev-drakkar.aretemed.io/"
        val token = properties?.token
        return WebClient.builder()
            .baseUrl(baseUrl)
            .defaultHeaders { headers ->
                headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                headers.set(HttpHeaders.AUTHORIZATION, "Token ${token}")
            }
            .clientConnector(ReactorClientHttpConnector(HttpClient.create()))
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

            val response = webClient()
                .get()
                .uri { uriBuilder -> uriBuilder.path("api/rooms/").queryParams(queryParams).build() }
                .exchange()
                .block()

            if (HttpStatus.OK == response?.statusCode()) {
                return response.bodyToMono(Rooms::class.java).block()!!
            } else {
                throw Throwable("${response?.statusCode()} \n ${response?.bodyToMono(String::class.java)?.block()}")
            }
        }

        /**
         * Getting a Room by id
         *
         * @param id The id of the desired Room
         * @return Room object representing the Room Entity itself @see io.aretemed.drakkar.model.Room
         */
        fun room(id: String): Room {
            val response = webClient()
                .get()
                .uri("api/rooms/${id}/")
                .exchange()
                .block()

            if (HttpStatus.OK == response?.statusCode()) {
                return response.bodyToMono(Room::class.java).block()!!
            } else {
                throw Throwable("${response?.statusCode()} \n ${response?.bodyToMono(String::class.java)?.block()}")
            }
        }

        /**
         * Creating a Room
         *
         * @param room The desired Room to be created
         * @return CreateRoomStatus object representing the status of creating the Room @see io.aretemed.drakkar.model.CreateRoomStatus
         */
        fun createRoom(room: Room): CreateRoomStatus {
            val response = webClient()
                .post()
                .uri("api/rooms/create-room/")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(room), Room::class.java)
                .exchange()
                .block()

            if (HttpStatus.OK == response?.statusCode()) {
                return response.bodyToMono(CreateRoomStatus::class.java).block()!!
            } else {
                throw Throwable("${response?.statusCode()} \n ${response?.bodyToMono(String::class.java)?.block()}")
            }
        }

        /**
         * Updating a Room
         *
         * @param room The desired Room to be updated to the passed state
         * @return Room object after update @see io.aretemed.drakkar.model.Room
         */
        fun updateRoom(room: Room): Room {
            val response = webClient()
                .put()
                .uri("api/rooms/${room.id}/update-room/")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(mapToRoomForUpdate(room)), RoomForUpdate::class.java)
                .exchange()
                .block()

            if (HttpStatus.OK == response?.statusCode()) {
                return response.bodyToMono(Room::class.java).block()!!
            } else {
                throw Throwable("${response?.statusCode()} \n ${response?.bodyToMono(String::class.java)?.block()}")
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
            val response = webClient()
                .post()
                .uri("api/rooms/${createMeetingTokenInfo.roomId}/create-meeting-token/")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(createMeetingTokenInfo), CreateMeetingTokenInfo::class.java)
                .exchange()
                .block()

            if (HttpStatus.OK == response?.statusCode()) {
                return response.bodyToMono(CreateMeetingTokenStatus::class.java).block()!!
            } else {
                throw Throwable("${response?.statusCode()} \n ${response?.bodyToMono(String::class.java)?.block()}")
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

            val response = webClient()
                .get()
                .uri { uriBuilder -> uriBuilder.path("api/encounters/").queryParams(queryParams).build() }
                .exchange()
                .block()

            if (HttpStatus.OK == response?.statusCode()) {
                return response.bodyToMono(Encounters::class.java).block()!!
            } else {
                throw Throwable("${response?.statusCode()} \n ${response?.bodyToMono(String::class.java)?.block()}")
            }
        }

        /**
         * Getting an Encounter by id
         *
         * @param id The id of the desired Encounter
         * @return Encounter object representing the Encounter Entity itself @see io.aretemed.drakkar.model.Encounter
         */
        fun encounter(id: String): Encounter {
            val response = webClient()
                .get()
                .uri("api/encounters/${id}/")
                .exchange()
                .block()

            if (HttpStatus.OK == response?.statusCode()) {
                return response.bodyToMono(Encounter::class.java).block()!!
            } else {
                throw Throwable("${response?.statusCode()} \n ${response?.bodyToMono(String::class.java)?.block()}")
            }
        }
    }

}