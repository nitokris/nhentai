package com.nitokrisalpha.infranstructure.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import kotlin.io.path.Path

@Component
@ConfigurationProperties("nhentai.path")
class PathConfig(
    var tmpPath: String = "/tmp/nhentai",
    var previews: String = "/app/static/previews",
    var galleries: String = "/app/static/galleries",
    var files: String = "/app/static/files",
    var qbBitTorrentDownloadPath: String = "/app/static/qbittorrent"
) {
    init {
        Path(tmpPath).toFile().mkdirs()
        Path(previews).toFile().mkdirs()
        Path(galleries).toFile().mkdirs()
        Path(files).toFile().mkdirs()
        Path(qbBitTorrentDownloadPath).toFile().mkdir()
    }
}