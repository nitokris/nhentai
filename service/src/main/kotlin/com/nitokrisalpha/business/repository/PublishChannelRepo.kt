package com.nitokrisalpha.business.repository

import com.nitokrisalpha.business.entity.PublishChannel

interface PublishChannelRepo {

    fun findOne(channelId: Long): PublishChannel?

    fun save(channels: List<PublishChannel>)

}