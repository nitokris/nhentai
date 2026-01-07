package com.nitokrisalpha.application.adapter.db.exposed

import org.jetbrains.exposed.v1.core.dao.id.LongIdTable

object FanzaConfigDB : LongIdTable("fanza_config") {
    val cookie = text("cookie")
    val type = varchar("type", 100)
}