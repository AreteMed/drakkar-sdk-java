package io.aretemed.drakkarsdk.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "io.aretemed.drakkarsdk")
data class DrakkarSDKWebClientProperties(
    var baseUrl: String?,
    var username: String?,
    var password: String?,
    var responseTimeout: Long?
)
