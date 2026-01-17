package com.nitokrisalpha.application.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "nhentai.qbittorrent")
data class QBitTorrentProperties(
    var url: String = "http://localhost:8080",
    var username: String = "admin",
    var password: String = "adminadmin"
)