package com.example.webclientconsumerkotlinsample.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Room(
    val id: String? = null,
    val name: String? = null,
    val business: String? = null,
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
    @JsonProperty("expiration_date_time")
    var expirationDateTime: Date? = null,
    @JsonProperty("meeting_join_hook")
    var meetingJoinHook: Date? = null,
    @JsonProperty("not_before_date_time")
    var notBeforeDateTime: Date? = null,
    val created: Date? = null,
    val updated: Date? = null
) {
    constructor() : this(id = null) {

    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Rooms(
    var count: Long,
    var next: String?,
    var previous: String?,
    var results: List<Room>?
)