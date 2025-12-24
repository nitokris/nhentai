package com.nitokrisalpha.business.thirdpart

import com.nitokrisalpha.business.entity.Circle
import com.nitokrisalpha.business.entity.Work

interface PublishChannelApi {

    fun circleWorks(circle: Circle, sort: Sort = Sort.DATE): List<Work>

    fun workDetail(work: Work): Work
}