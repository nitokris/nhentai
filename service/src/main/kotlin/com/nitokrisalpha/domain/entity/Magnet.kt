package com.nitokrisalpha.domain.entity

import com.fasterxml.jackson.annotation.JsonUnwrapped

class Magnet(
    @get:JsonUnwrapped
    val id: MagnetId,
    val metadata: MagnetMetaData,
    var progress: Int = 0,
    var downLoadStatus: DownLoadStatus = DownLoadStatus.QUEUED,
    var errorMessage: String = ""
) {

    init {
        if (downLoadStatus != DownLoadStatus.FAILED && errorMessage.isNotEmpty()) {
            throw IllegalArgumentException("Error message must be empty if download status is not FAILED")
        }
    }

    enum class DownLoadStatus {
        QUEUED,
        DOWNLOADING,
        COMPLETED,
        FAILED
    }

}