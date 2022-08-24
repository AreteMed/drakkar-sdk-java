package io.aretemed.drakkar.model

import com.example.webclientconsumerkotlinsample.model.Room
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Encounter(
    val id: String? = null,
    val room: String? = null,
    @JsonProperty("actual_start_date_time")
    var actualStartDateTime: Date? = null,
    @JsonProperty("actual_end_date_time")
    var actualEndDateTime: Date? = null,
    @JsonProperty("scheduled_start_date_time")
    var scheduledStartDateTime: Date? = null,
    var duration: Int? = null,
    val created: String? = null,
    val updated: Date? = null
) {
    constructor() : this(id = null) {

    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Encounters(
    var count: Long,
    var next: String?,
    var previous: String?,
    var results: List<Encounter>?
)