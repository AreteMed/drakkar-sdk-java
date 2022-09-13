package io.aretemed.drakkar

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.aretemed.drakkar.model.Room
import io.aretemed.drakkar.client.DrakkarWebClient
import io.aretemed.drakkar.model.CreateMeetingTokenInfo
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
        val jsonMapper = jacksonObjectMapper()
        val callAPI = args?.getOptionValues("callAPI")?.first()
        val roomId = args?.getOptionValues("roomId")?.first()
        val limit = args?.getOptionValues("limit")?.first()
        val offset = args?.getOptionValues("offset")?.first()
        val enableKnocking = args?.getOptionValues("enableKnocking")?.first()
        val userId = args?.getOptionValues("userId")?.first()
        val userName = args?.getOptionValues("userName")?.first()
        val isOwner = args?.getOptionValues("isOwner")?.first()
        val encounterId = args?.getOptionValues("encounterId")?.first()

        val response =
            when (callAPI) {
                "rooms" -> jsonMapper.writeValueAsString(
                    drakkarWebClient.roomAPI().rooms((limit ?: "0").toInt(), (offset ?: "0").toInt())
                )
                "roomById" -> jsonMapper.writeValueAsString(drakkarWebClient.roomAPI().room(roomId!!))
                "createRoom" -> jsonMapper.writeValueAsString(drakkarWebClient.roomAPI().createRoom(Room()))
                "updateRoomKnocking" -> {
                    val room = drakkarWebClient.roomAPI().room(roomId!!)
                    room.enableKnocking = enableKnocking.toBoolean()
                    jsonMapper.writeValueAsString(drakkarWebClient.roomAPI().updateRoom(room))
                }
                "updateRoomJoinHookToNull" -> {
                    val room = drakkarWebClient.roomAPI().room(roomId!!)
                    room.meetingJoinHook = null
                    jsonMapper.writeValueAsString(drakkarWebClient.roomAPI().updateRoom(room))
                }
                "createMeetingToken" -> jsonMapper.writeValueAsString(
                    drakkarWebClient.roomAPI().createMeetingToken(
                        CreateMeetingTokenInfo(
                            roomId = roomId!!,
                            userId = userId!!,
                            userName = userName!!,
                            isOwner = isOwner.toBoolean()
                        )
                    )
                )
                "encounters" -> jsonMapper.writeValueAsString(
                    drakkarWebClient.encounterAPI().encounters((limit ?: "0").toInt(), (offset ?: "0").toInt())
                )
                "encounterById" -> jsonMapper.writeValueAsString(drakkarWebClient.encounterAPI().encounter(encounterId!!))
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
