package io.aretemed.drakkar.config

import io.aretemed.drakkar.custom.Generated
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties

@SpringBootConfiguration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "io.aretemed.drakkar")
@Generated
class DrakkarWebClientProperties{
    var baseUrl: String? = null
    var token: String? = null
}