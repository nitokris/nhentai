package com.nitokrisalpha.infranstructure.init

import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import qbittorrent.QBittorrentClient


//@Component
class InitQBittorrentClient {

    @EventListener(value = [ApplicationReadyEvent::class])
    fun start() {
        suspend {
            val client = QBittorrentClient("")
            client.login()
            client.observeMainData().collect {
            }
        }
    }
}