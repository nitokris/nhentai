package com.nitokrisalpha.infranstructure.config

import com.nitokrisalpha.common.logger
import org.apache.commons.io.FileUtils
import org.apache.commons.io.FileUtils.toFile
import org.apache.commons.io.file.PathUtils
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import java.nio.file.Files
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
        PathUtils.createParentDirectories(Path(tmpPath).normalize())
        logger.info("start create previews path:{}", previews)
        PathUtils.createParentDirectories(Path(previews).normalize())
        logger.info("start create galleries path:{}", galleries)
        PathUtils.createParentDirectories(Path(galleries).normalize())
        logger.info("start create files path:{}", files)
        PathUtils.createParentDirectories(Path(files).normalize())
        logger.info("start create qbBitTorrentDownloadPath:{}", qbBitTorrentDownloadPath)
        PathUtils.createParentDirectories(Path(qbBitTorrentDownloadPath).normalize())
    }
}