package com.nitokrisalpha.application.configuration

import org.apache.commons.lang3.StringUtils
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "comic.api.fanza")
data class FANZAApiProperties(
    var cookie: String = "",
    var userAgent: String = "",
    var dnt: String = ""
) {


    fun afterInit() {
        if (StringUtils.isEmpty(cookie)) {
            throw RuntimeException("could not initialize FANZAApiProperties cookie is empty!")
        }
    }

}
