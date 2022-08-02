package com.example.webclientconsumerkotlinsample.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Room(
    var id: String?,
    var name: String?,
    var business: String?,
    @JsonProperty("enable_chat")
    var enableChat: Boolean?,
    @JsonProperty("enable_knocking")
    var enableKnocking: Boolean?,
    @JsonProperty("enable_people_ui")
    var enablePeopleUI: Boolean?,
    @JsonProperty("enable_prejoin_ui")
    var enablePrejoinUI: Boolean?,
    @JsonProperty("enable_screenshare")
    var enableScreenshare: Boolean?,
    @JsonProperty("expiration_date_time")
    var expirationDateTime: Date?,
    @JsonProperty("meeting_join_hook")
    var meetingJoinHook: Date?,
    @JsonProperty("not_before_date_time")
    var notBeforeDateTime: Date?,
    var created: Date?,
    var updated: Date?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Rooms(
    var count: Long,
    var next: String?,
    var previous: String?,
    var results: List<Room>?
)