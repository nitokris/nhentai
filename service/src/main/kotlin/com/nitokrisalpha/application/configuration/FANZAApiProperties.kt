package com.nitokrisalpha.application.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "comic.api.fanza")
data class FANZAApiProperties(
    var cookie: String = "",
    var userAgent: String = "",
    var dnt: String = ""
)
