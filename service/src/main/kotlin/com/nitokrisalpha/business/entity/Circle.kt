package com.nitokrisalpha.business.entity


class Circle(
    val name: String
) {
    private val _channels: MutableList<PublishChannel> = mutableListOf()

    private val _works: MutableList<Work> = mutableListOf()

    val channels: List<PublishChannel>
        get() = _channels.toList()

    val works: List<Work>
        get() = _works.toList()

    fun registration2Channel(channel: PublishChannel) {
        if (!_channels.contains(channel)) {
            _channels.add(channel)
        }
    }

    fun publishWork(work: Work, channelId: String): Publish {
        if (!_works.contains(work)) {
            _works.add(work)
        }
        val channel = _channels.first { it.identifier == channelId }
        return work.bePublish2Channel(channel)
    }

}