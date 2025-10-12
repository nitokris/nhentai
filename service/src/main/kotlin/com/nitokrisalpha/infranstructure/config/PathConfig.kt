package com.nitokrisalpha.infranstructure.config

import com.nitokrisalpha.common.logger
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
        logger.info("start create tmp path:{}", tmpPath)
        Path(tmpPath).toFile().mkdirs()
        logger.info("start create previews path:{}", previews)
        Path(previews).toFile().mkdirs()
        Path(galleries).toFile().mkdirs()
        Path(files).toFile().mkdirs()
        Path(qbBitTorrentDownloadPath).toFile().mkdir()
    }
}