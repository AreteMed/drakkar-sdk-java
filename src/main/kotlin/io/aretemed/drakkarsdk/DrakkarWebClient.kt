package io.aretemed.drakkarsdk

import com.example.webclientconsumerkotlinsample.model.Rooms
import com.example.webclientconsumerkotlinsample.model.Token
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
    private lateinit var properties : DrakkarSDKWebClientProperties

    private var jwtToken: Token? = null
    
    private fun webClient(): WebClient {
        val client = HttpClient.create()
            .responseTimeout(Duration.ofSeconds(properties.responseTimeout ?: 60L))
        return WebClient.builder()
            .baseUrl(properties.baseUrl ?: "https://dev-drakkar.aretemed.io/")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .clientConnector(ReactorClientHttpConnector(client))
            .build()
    }

    fun createToken(username: String, password: String): Token? {
        return webClient()
            .post()
            .uri("api/token/")
            .retrieve()
            .bodyToMono(Token::class.java)
            .block()
    }

    fun auth() {
        jwtToken = createToken(properties.username?:"DevUser", properties.password?:"DevPassword")
    }

    private fun applyAuth(headers: HttpHeaders) {
        jwtToken?.let { headers.setBearerAuth(it.access) }
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
            .uri("api/rooms", queryParams)
            .headers { headers -> applyAuth(headers) }
            .retrieve()
            .bodyToMono(Rooms::class.java)
            .block()
    }
}