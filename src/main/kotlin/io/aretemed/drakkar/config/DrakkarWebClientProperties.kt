package io.aretemed.drakkar.config

import io.aretemed.drakkar.custom.Generated
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties

@SpringBootConfiguration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "io.aretemed.drakkar")
@Generated
data class DrakkarWebClientProperties(
    var baseUrl: String?,
    var token: String?,
    var responseTimeout: Long?
)
