package com.nitokrisalpha.business.entity

class Circle(
    val name: String,
    private var state: State = State.WAIT_CHECK,
    private val publications: MutableList<Publication> = mutableListOf()
) : BaseEntity() {
    enum class State(
        override val codeValue: String,
        override val codeName: String
    ) : BaseStatus {
        WAIT_CHECK("WAIT_CHECK", "等待检测"),
        CHECKING("CHECKING", "检测中"),
        WAITING_UPDATE_WORKS("WAITING_UPDATE_WORKS", "等待更新作品"),
        FETCHING_WORKS("FETCHING_WORKS", "获取作品中"),
        CHECKED("CHECKED", "检测完成"),
    }

    public fun joinToChannel(publishChannel: PublishChannel, channelCircleId: String): Join {
        return Join(channelCircleId, this, publishChannel)
    }

    public fun publish(work: Work, to: PublishChannel): Publication {
        val p = Publication()
        publications += p
        return p
    }
}