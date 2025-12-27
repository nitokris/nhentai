package com.nitokrisalpha.business.entity

import java.util.*

data class WorkId(
    override val value: String = UUID.randomUUID().toString()
) : Id<String>