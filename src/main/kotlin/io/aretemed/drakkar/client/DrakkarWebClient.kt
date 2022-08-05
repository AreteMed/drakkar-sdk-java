package io.aretemed.drakkar.client

import com.example.webclientconsumerkotlinsample.model.Room
import com.example.webclientconsumerkotlinsample.model.Rooms
import io.aretemed.drakkar.config.DrakkarWebClientProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.netty.http.client.HttpClient
import java.time.Duration

@Component
class DrakkarWebClient {

    @Autowired
    var properties : DrakkarWebClientProperties? = null
    
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

    fun rooms(limit: Long? = 0L, offset: Long? = 0L): Rooms? {
        val queryParams = mutableMapOf<String, Any>()
        if (limit != null && limit > 0){
            queryParams["limit"] = limit
        }
        if (offset != null && offset > 0){
            queryParams["offset"] = offset
        }

        return webClient()
            .get()
            .uri("api/rooms/", queryParams)
            .retrieve()
            .bodyToMono(Rooms::class.java)
            .block()
    }

    fun room(id: String): Room? {
        return webClient()
            .get()
            .uri("api/rooms/${id}")
            .retrieve()
            .bodyToMono(Room::class.java)
            .block()
    }

    fun createRoom(room: Room): Room? {
        return webClient()
            .post()
            .uri("api/rooms/create-room/")
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(room), Room::class.java)
            .retrieve()
            .bodyToMono(Room::class.java)
            .block()
    }

    fun updateRoom(room: Room): Room? {
        return webClient()
            .put()
            .uri("api/rooms/${room.id}/update-room/")
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(room), Room::class.java)
            .retrieve()
            .bodyToMono(Room::class.java)
            .block()
    }

    fun deleteRoom(room: Room) : Void? {
        return webClient()
            .delete()
            .uri("api/rooms/${room.id}")
            .retrieve()
            .bodyToMono(Void::class.java)
            .block()
    }

}