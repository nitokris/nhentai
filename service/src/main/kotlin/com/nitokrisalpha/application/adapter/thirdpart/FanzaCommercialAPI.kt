package com.nitokrisalpha.application.adapter.thirdpart

import com.nitokrisalpha.business.entity.Circle
import com.nitokrisalpha.business.entity.PublishChannel
import com.nitokrisalpha.business.entity.Work
import com.nitokrisalpha.business.thirdpart.PublishChannelApi
import com.nitokrisalpha.business.thirdpart.Sort
import org.http4k.core.HttpHandler
import org.springframework.stereotype.Component

@Component
class FanzaCommercialAPI(

    private val client: HttpHandler

) : PublishChannelApi {
    override fun circleWorks(
        circle: Circle,
        sort: Sort
    ): List<Work> {
        TODO("Not yet implemented")
    }

    override fun workDetail(channel: PublishChannel): Work {
        TODO("Not yet implemented")
    }
}