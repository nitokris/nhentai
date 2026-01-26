package com.nitokrisalpha.business.entity

import java.util.*

class Resource(
    val size: String,
    val sharing: Date,
    val accessUrl: String,
    val type: String,
    val name: String,
    private var _status: Status = Status.WAIT_DOWNLOAD
) {

    val status: Status
        get() = _status

    enum class Status(
        override val codeValue: String,
        override val codeName: String,
    ) : BaseStatus {
        WAIT_DOWNLOAD("WAIT_DOWNLOAD", "等待下载"),
        DOWNLOADING("DOWNLOADING", "下载中"),
        DOWNLOADED("DOWNLOADED", "已下载"),
        PROCESSING("PROCESSING", "处理中"),
        PROCESSED("PROCESSED", "已处理"),
    }


}