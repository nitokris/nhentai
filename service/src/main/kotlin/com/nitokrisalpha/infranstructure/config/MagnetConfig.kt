package com.nitokrisalpha.infranstructure.config

import com.nitokrisalpha.domain.service.MagnetProviderImpl
import org.http4k.core.HttpHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MagnetConfig {

    @Bean
    fun magnetProvider(client: HttpHandler) = MagnetProviderImpl(client)
}