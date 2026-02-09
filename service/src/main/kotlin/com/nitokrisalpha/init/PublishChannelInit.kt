package com.nitokrisalpha.init

import com.nitokrisalpha.business.entity.PublishChannel
import com.nitokrisalpha.business.repository.PublishChannelRepo
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class PublishChannelInit(
    private val publishChannelRepo: PublishChannelRepo,
) : ApplicationRunner {

    companion object {
        val defaultChannels = listOf(
            PublishChannel("https://www.dmm.co.jp/dc/doujin/", "FANZA_DOUJIN"),
        )
    }

    @Transactional
    override fun run(args: ApplicationArguments?) {
        publishChannelRepo.save(defaultChannels)
    }
}