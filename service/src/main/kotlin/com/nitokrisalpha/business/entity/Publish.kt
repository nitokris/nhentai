package com.nitokrisalpha.business.entity

import java.time.LocalDateTime


data class Publish(
    val channel: PublishChannel,
    val work: Work,
    val publishDate: LocalDateTime,
    var updateDate: LocalDateTime,
)