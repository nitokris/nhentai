package com.nitokrisalpha.business.entity

class Join(
    val channelId: String,
    val circle: Circle,
    val publishChannel: PublishChannel,
    private val _status: Status = Status.UNTRACKING
) {
    val status: Status
        get() = _status

    enum class Status(
        override val codeValue: String,
        override val codeName: String
    ) : BaseStatus {
        TRACKING("tracking", "追踪中"),
        UNTRACKING("untracking", "未追踪"),
    }
}