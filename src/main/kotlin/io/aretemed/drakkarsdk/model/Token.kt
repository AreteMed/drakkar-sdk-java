package com.example.webclientconsumerkotlinsample.model

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Token(
    var refresh: String,
    var access: String,
)
