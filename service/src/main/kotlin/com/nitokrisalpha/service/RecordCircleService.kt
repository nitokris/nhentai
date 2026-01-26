package com.nitokrisalpha.service

import com.nitokrisalpha.business.entity.Circle
import com.nitokrisalpha.business.repository.CircleRepo
import com.nitokrisalpha.business.repository.JoinRepo
import com.nitokrisalpha.business.repository.PublishChannelRepo
import com.nitokrisalpha.values.param.RecordCircleParam
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class RecordCircleService(
    private val publishChannelRepo: PublishChannelRepo,
    private val circleRepo: CircleRepo,
    private val joinRepo: JoinRepo
) {


    fun recordCircle(param: RecordCircleParam) {
        val publishChannelId = param.publishChannelId
        val channel =
            publishChannelRepo.findOne(publishChannelId) ?: throw RuntimeException("failed to find publish channel!")
        val circle = Circle(param.name)
        val join = circle.joinToChannel(channel, param.channelCircleId)
        circleRepo.save(circle)
        joinRepo.save(join)
    }

}