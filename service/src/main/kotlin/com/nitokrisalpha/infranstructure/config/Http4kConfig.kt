package com.nitokrisalpha.infranstructure.config

import com.nitokrisalpha.common.logger
import org.apache.hc.client5.http.config.RequestConfig
import org.apache.hc.client5.http.cookie.StandardCookieSpec.IGNORE
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.core5.http.HttpHost
import org.http4k.client.ApacheClient
import org.http4k.core.HttpHandler
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component

@ConfigurationProperties(prefix = "nhentai.http4k")
data class Http4kConfigProperties(
    val proxyHost: String,
    val proxyPort: Int,
    val proxyEnabled: Boolean
)

@Configuration
@ConditionalOnClass(value = [ApacheClient::class])
class Http4kConfig(
    private val properties: Http4kConfigProperties
) {

    init {
        if (properties.proxyEnabled && (properties.proxyHost.isBlank() || properties.proxyPort == 0)) {
            throw IllegalArgumentException("Proxy is enabled but host or port is not set")
        }
        if (properties.proxyEnabled) {
            logger.info("Http4kConfig: proxy is enabled, host: ${properties.proxyHost}, port: ${properties.proxyPort}")
        } else {
            logger.info("Http4kConfig: proxy is disabled")
        }
    }

    @Bean
    fun client(): HttpHandler {
        val builder = HttpClients.custom()
            .setDefaultRequestConfig(
                RequestConfig.custom()
                    .setRedirectsEnabled(false)
                    .setCookieSpec(IGNORE)
                    .build()
            )
        if (properties.proxyEnabled) {
            builder.setProxy(HttpHost(properties.proxyHost, properties.proxyPort))
        }
        return ApacheClient(client = builder.build())
    }

}