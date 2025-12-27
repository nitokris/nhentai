package com.nitokrisalpha.business.entity

import java.time.LocalDateTime

class Work(
    // 标题
    val title: String = "",
    // 封面
    val cover: String = "",
    // 作品形式（同人志/ASMR）
    val format: String = "",
    // 所属系列
    val series: String = "",
    // 简介
    val summary: String = "",
    // 标签
    val tags: Set<String> = mutableSetOf(),
    // 预览图
    val images: Set<String> = mutableSetOf(),
    // 題材
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
        return "Work(channels=$_channels, title='${title}', images=$images, summary='${summary}')"
    }

}