package com.nitokrisalpha.application.adapter.fanza

import com.nitokrisalpha.application.configuration.FANZAApiProperties
import org.http4k.core.Method
import org.http4k.core.Request
import org.springframework.stereotype.Component

@Component
class FazaRequestBuilder(
    private val fanzaApiProperties: FANZAApiProperties
) {

    fun createGet(url: String): Request {
        return Request(Method.GET, url)
            .header("cookie", fanzaApiProperties.cookie)
            .header("user-agent", fanzaApiProperties.userAgent)
            .header("dnt", fanzaApiProperties.dnt)
    }

}