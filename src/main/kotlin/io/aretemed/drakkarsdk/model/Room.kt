package com.example.webclientconsumerkotlinsample.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Room(
    var business: String,
    var created: String,
    @JsonProperty("enable_chat")
    var enableChat: Boolean,
    @JsonProperty("enable_knocking")
    var enableKnocking: Boolean,
    @JsonProperty("enable_people_ui")
    var enablePeopleUI: Boolean,
    @JsonProperty("enable_prejoin_ui")
    var enablePrejoinUI: Boolean,
    @JsonProperty("enable_screenshare")
    var enableScreenshare: Boolean,
    @JsonProperty("expiration_date_time")
    var expirationDateTime: Date?,
    var id: String,
    @JsonProperty("meeting_join_hook")
    var meetingJoinHook: Date?,
    var name: String,
    @JsonProperty("not_before_date_time")
    var notBeforeDateTime: Date?,
    var updated: String
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Rooms(
    var count: Long,
    var next: String?,
    var previous: String?,
    var results: List<Room>?
)