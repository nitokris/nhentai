package com.nitokrisalpha.application.adapter.fanza

import com.nitokrisalpha.business.entity.Circle
import com.nitokrisalpha.business.entity.PublishChannel
import com.nitokrisalpha.business.entity.WorkMetadata
import com.nitokrisalpha.business.thirdpart.PublishChannelApi
import com.nitokrisalpha.business.thirdpart.Sort
import org.http4k.core.HttpHandler
import org.springframework.stereotype.Component

@Component
class DMMBookAPI(

    private val client: HttpHandler

) : PublishChannelApi {

    companion object {
        const val BASE_URL = "https://book.dmm.co.jp/list/?author="
        const val CHANNEL_ID = "dmm_book"
    }

    override fun circleWorks(
        circle: Circle,
        sort: Sort
    ): List<WorkMetadata> {
        val channel: PublishChannel = circle.channels.find { it.name == CHANNEL_ID } ?: return emptyList()
        val url = BASE_URL +channel.identifier

        TODO("Not yet implemented")
    }

    override fun workDetail(channel: PublishChannel): WorkMetadata {
        TODO("Not yet implemented")
    }
}