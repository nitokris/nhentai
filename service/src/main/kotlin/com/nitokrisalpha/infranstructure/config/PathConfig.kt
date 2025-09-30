package com.nitokrisalpha.infranstructure.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("nhentai.path")
class PathConfig(
    val tmpPath: String = "/tmp/nhentai",
    val previews: String = "/app/static/previews",
    val galleries: String = "/app/static/galleries",
    val files: String = "/app/static/files"
) {
}