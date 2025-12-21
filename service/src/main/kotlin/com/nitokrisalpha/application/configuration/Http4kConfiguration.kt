package com.nitokrisalpha.application.configuration

import org.http4k.client.ApacheClient
import org.http4k.core.HttpHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Http4kConfiguration {

    @Bean
    fun httpHandler(): HttpHandler {
        return ApacheClient()
    }

}