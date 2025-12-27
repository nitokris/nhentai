package com.nitokrisalpha.business.entity

import java.time.LocalDateTime

class Work(
    val id: WorkId,
    val metaData: WorkMetadata,
) {

    companion object {
        val DUMMY = Work(WorkId("DUMMY"), WorkMetadata.EMPTY)
    }

    private val _channels: MutableList<PublishChannel> = mutableListOf()

    val channels: List<PublishChannel>
        get() = _channels.toList()

    fun bePublish2Channel(
        channel: PublishChannel,
        publishDate: LocalDateTime = LocalDateTime.now(),
        updateDate: LocalDateTime = LocalDateTime.now()
    ): Publish {
        if (!_channels.contains(channel)) {
            _channels.add(channel)
        }
        return Publish(
            channel = channel,
            work = this,
            publishDate = publishDate,
            updateDate = updateDate
        )
    }

    override fun toString(): String {
        return "Work(id=$id, metaData=$metaData, _channels=$_channels, channels=$channels)"
    }

}