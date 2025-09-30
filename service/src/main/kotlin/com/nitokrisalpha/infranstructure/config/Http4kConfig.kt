package com.nitokrisalpha.infranstructure.config

import org.http4k.client.ApacheClient
import org.http4k.core.HttpHandler
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnClass(value = [ApacheClient::class])
class Http4kConfig {

    @Bean
    fun client(): HttpHandler {
        return ApacheClient()
    }

}