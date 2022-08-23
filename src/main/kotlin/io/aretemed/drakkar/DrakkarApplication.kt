package io.aretemed.drakkar

import com.example.webclientconsumerkotlinsample.model.Room
import com.google.gson.Gson
import io.aretemed.drakkar.client.DrakkarWebClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.Banner
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder

@SpringBootApplication
class DrakkarApplication: ApplicationRunner {

    @Autowired
    private lateinit var drakkarWebClient: DrakkarWebClient

    override fun run(args: ApplicationArguments?) {
        val callAPI = args?.getOptionValues("callAPI")?.first()
        val response =
            when (callAPI) {
                "rooms" -> Gson().toJson(drakkarWebClient.rooms())
                "createRoom" -> Gson().toJson(drakkarWebClient.createRoom(Room()))
                else -> ""
            }

        println("=====================================The Result is:=====================================")
        println(response)
    }

}


fun main(args: Array<String>) {
    SpringApplicationBuilder(DrakkarApplication::class.java)
        .bannerMode(Banner.Mode.OFF)
        .web(WebApplicationType.NONE)
        .run(*args)
}
