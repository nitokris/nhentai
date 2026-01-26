package com.nitokrisalpha.business.entity

import java.util.*

open class BaseEntity(
    val id: Long? = 1,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
) {

}