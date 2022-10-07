package io.aretemed.drakkar.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import io.aretemed.drakkar.custom.Generated
import java.util.*

/**
 * An Entity representing "Drakkar Room"
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated
data class Room(
    val id: String? = null,
    val name: String? = null,
    val business: String? = null,
    @JsonProperty("not_before_date_time")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    var notBeforeDateTime: Date? = null,
    @JsonProperty("expiration_date_time")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    var expirationDateTime: Date? = null,
    @JsonProperty("enable_chat")
    var enableChat: Boolean? = null,
    @JsonProperty("enable_knocking")
    var enableKnocking: Boolean? = null,
    @JsonProperty("enable_people_ui")
    var enablePeopleUI: Boolean? = null,
    @JsonProperty("enable_prejoin_ui")
    var enablePrejoinUI: Boolean? = null,
    @JsonProperty("enable_screenshare")
    var enableScreenshare: Boolean? = null,
    @JsonProperty("meeting_join_hook")
    var meetingJoinHook: String? = null,
    var url: String? = null,
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    val created: Date? = null,
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    val updated: Date? = null
) {
    constructor() : this(id = null) {

    }
}

@Generated
data class RoomForUpdate(
    @JsonProperty("not_before_date_time")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    var notBeforeDateTime: Date? = null,
    @JsonProperty("expiration_date_time")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    var expirationDateTime: Date? = null,
    @JsonProperty("enable_chat")
    var enableChat: Boolean? = null,
    @JsonProperty("enable_knocking")
    var enableKnocking: Boolean? = null,
    @JsonProperty("enable_people_ui")
    var enablePeopleUI: Boolean? = null,
    @JsonProperty("enable_prejoin_ui")
    var enablePrejoinUI: Boolean? = null,
    @JsonProperty("enable_screenshare")
    var enableScreenshare: Boolean? = null,
    @JsonProperty("meeting_join_hook")
    var meetingJoinHook: String? = null
)
/**
 * The Status of Creating Room
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated
data class CreateRoomStatus(
    val success: Boolean,
    val url: String? = null,
    val id: String? = null,
)

/**
 * The Status of Creating Meeting Token
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated
data class CreateMeetingTokenStatus(
    val success: Boolean,
    val url: String? = null,
)

/**
 * A Holder of params for Creating Meeting Token
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated
data class CreateMeetingTokenInfo(
    @JsonIgnore
    val roomId: String,
    @JsonProperty("user_id")
    val userId: String,
    @JsonProperty("user_name")
    val userName: String,
    @JsonProperty("is_owner")
    val isOwner: Boolean
)

/**
 * A Holder of info about available "Rooms"
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated
data class Rooms(
    var count: Long,
    var next: String?,
    var previous: String?,
    var results: List<Room>?
)