package com.nitokrisalpha.infranstructure.timer

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class MetaDataRefreshTimer {

    @Scheduled(cron = "0 0 * * * *")
    fun refresh() {

    }

}