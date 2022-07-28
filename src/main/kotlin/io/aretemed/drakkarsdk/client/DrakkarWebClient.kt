package io.aretemed.drakkarsdk.client

import com.example.webclientconsumerkotlinsample.model.Rooms
import io.aretemed.drakkarsdk.config.DrakkarSDKWebClientProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import java.time.Duration

@Component
class DrakkarWebClient {

    @Autowired
    var properties : DrakkarSDKWebClientProperties? = null
    
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
}