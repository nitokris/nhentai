package com.nitokrisalpha.application.adapter.magnet

import com.nitokrisalpha.application.configuration.QBitTorrentProperties
import com.nitokrisalpha.business.entity.Resource
import com.nitokrisalpha.business.thirdpart.ResourceFetchApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import qbittorrent.QBittorrentClient
import kotlin.concurrent.thread

//@Component
class QBittorrentAdapter(
    prop: QBitTorrentProperties
) : ResourceFetchApi {

    private val client: QBittorrentClient = QBittorrentClient(prop.url, prop.username, prop.password)
    private var defaultSavePath = ""

    companion object {
        private val MAGNET_HASH_REGEX =
            Regex("""(?i)\bxt=urn:btih:([a-f0-9]{40}|[a-z2-7]{32})\b""")

        fun extractBtihHash(magnet: String): String? {
            return MAGNET_HASH_REGEX
                .find(magnet)
                ?.groupValues
                ?.get(1)
                ?.lowercase()
        }
    }

    init {
        runBlocking(Dispatchers.IO) {
            client.login()
            defaultSavePath = client.getDefaultSavePath()
        }
        thread(isDaemon = true) {
            runBlocking(Dispatchers.IO) {
                client.observeMainData().collect { mainData ->
                    // Handle main data updates if needed
                    mainData.torrents.forEach { entry ->
                        val torrent = entry.value
                        if (torrent.progress == 1.0f) {

                        }

                    }
                }
            }

        }
    }

    override fun fetchResource(resource: Resource) {
        runBlocking(Dispatchers.IO) {
            client.addTorrent {
                urls += resource.position
                extractBtihHash(resource.position)?.let { hash ->
                    savepath = "${defaultSavePath}/${hash}"
                }
            }
        }
    }
}