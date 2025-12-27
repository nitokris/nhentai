package com.nitokrisalpha.business.entity

import jdk.javadoc.internal.doclets.formats.html.markup.HtmlStyle
import java.time.LocalDateTime

class Work(
    val title: String = "",
    val cover: String = "",
    val format: String = "",
    val series: String = "",
    val summary: String = "",
    val tags: Set<String> = mutableSetOf(),
    val images: Set<String> = mutableSetOf(),
    val subjectMatter: String = ""
) {

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
        return "Work(channels=$_channels, title='${HtmlStyle.title}', images=$images, summary='${HtmlStyle.summary}')"
    }

}