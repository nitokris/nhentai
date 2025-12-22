package com.nitokrisalpha.application.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "comic.third-part.sukebei")
data class SukebeiApiProperties(
    var cookie: String = ""
) {
}