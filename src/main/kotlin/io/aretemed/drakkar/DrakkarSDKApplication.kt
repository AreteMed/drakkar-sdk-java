package io.aretemed.drakkar

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
class DrakkarSDKApplication: ApplicationRunner {

    @Autowired
    private lateinit var drakkarWebClient: DrakkarWebClient

    override fun run(args: ApplicationArguments?) {
        val callAPI = args?.getOptionValues("callAPI")?.first()
        val response =
            when (callAPI) {
                "rooms" -> Gson().toJson(drakkarWebClient.rooms())
                else -> ""
            }

        println(response)
    }

}


fun main(args: Array<String>) {
    SpringApplicationBuilder(DrakkarSDKApplication::class.java)
        .bannerMode(Banner.Mode.OFF)
        .web(WebApplicationType.NONE)
        .run(*args)
}
