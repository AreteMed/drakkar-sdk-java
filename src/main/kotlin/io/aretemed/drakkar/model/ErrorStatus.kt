package io.aretemed.drakkar.model

data class ErrorStatus(
    val success: Boolean,
    val error: String,
    val info: String
)