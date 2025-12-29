package com.nitokrisalpha.business.thirdpart

import com.nitokrisalpha.business.entity.Circle
import com.nitokrisalpha.business.entity.PublishChannel
import com.nitokrisalpha.business.entity.WorkMetadata

interface PublishChannelApi {

    fun circleWorks(circle: Circle, sort: Sort = Sort.DATE): List<WorkMetadata>

    fun workDetail(channel: PublishChannel): WorkMetadata
}