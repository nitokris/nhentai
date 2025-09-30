package com.nitokrisalpha.common

import com.nitokrisalpha.domain.entity.Id
import java.util.*
import kotlin.reflect.full.primaryConstructor

inline fun <reified T : Id> newId(): T {
    val constructor = T::class.primaryConstructor!!
    return constructor.call(UUID.randomUUID().noSlashStr())
}
