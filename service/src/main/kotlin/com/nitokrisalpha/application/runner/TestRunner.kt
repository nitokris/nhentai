package com.nitokrisalpha.application.runner

import com.nitokrisalpha.application.adapter.thirdpart.FANZADoujinApi
import com.nitokrisalpha.application.logging.log
import com.nitokrisalpha.business.entity.PublishChannel
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class TestRunner(
    private val fanzaDoujinApi: FANZADoujinApi
) : ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        val channel = PublishChannel(FANZADoujinApi.CHANNEL_NAME, "d_691394")
        val workDetail = fanzaDoujinApi.workDetail(channel)
        log.info(workDetail.toString())
//        val circle = Circle("DOLL PLAY")
//        circle.registration2Channel(PublishChannel("FANZA_DOUJIN", "76046"))
//        val circleWorks = fanzaDoujinApi.circleWorks(circle = circle)
//        log.info("works:{}", circleWorks)
//        for (work in circleWorks) {
//            fanzaDoujinApi.workDetail(work)
//        }
    }
}