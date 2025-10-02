package com.nitokrisalpha.infranstructure.config

import com.nitokrisalpha.common.logger
import org.apache.hc.client5.http.config.RequestConfig
import org.apache.hc.client5.http.cookie.StandardCookieSpec.IGNORE
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.core5.http.HttpHost
import org.http4k.client.ApacheClient
import org.http4k.core.HttpHandler
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnClass(value = [ApacheClient::class])
@ConfigurationProperties(prefix = "nhentai.http4k")
class Http4kConfig(
    private val proxyHost: String = "127.0.0.1",
    private val proxyPort: Int = 7890,
    private val proxyEnabled: Boolean = false
) {
    init {
        if (proxyEnabled && (proxyHost.isBlank() || proxyPort == 0)) {
            throw IllegalArgumentException("Proxy is enabled but host or port is not set")
        }
        if (proxyEnabled) {
            logger.info("Http4kConfig: proxy is enabled, host: $proxyHost, port: $proxyPort")
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
        if(proxyEnabled){
            builder.setProxy(HttpHost(proxyHost, proxyPort))
        }
        return ApacheClient(client = builder.build())
    }

}