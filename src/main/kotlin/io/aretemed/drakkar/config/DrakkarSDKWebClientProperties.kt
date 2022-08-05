package io.aretemed.drakkar.config

import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties

@SpringBootConfiguration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "io.aretemed.drakkarsdk")
data class DrakkarSDKWebClientProperties(
    var baseUrl: String?,
    var token: String?,
    var responseTimeout: Long?
)
