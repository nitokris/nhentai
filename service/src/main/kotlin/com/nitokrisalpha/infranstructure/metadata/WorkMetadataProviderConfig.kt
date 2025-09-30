package com.nitokrisalpha.infranstructure.metadata

import com.nitokrisalpha.domain.service.DLSiteProviderImpl
import com.nitokrisalpha.domain.service.FanzaProviderImpl
import com.nitokrisalpha.domain.service.MelonBooksProvider
import org.http4k.core.HttpHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class WorkMetadataProviderConfig {


    @Bean
    fun fanzaProvider(handler: HttpHandler) = FanzaProviderImpl(handler)

    @Bean
    fun dlSiteProvider(handler: HttpHandler) = DLSiteProviderImpl(handler)

    @Bean
    fun melonBooksProvider(handler: HttpHandler) = MelonBooksProvider(handler)

}