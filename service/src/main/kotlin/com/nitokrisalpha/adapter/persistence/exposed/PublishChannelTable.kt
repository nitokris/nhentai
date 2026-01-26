package com.nitokrisalpha.adapter.persistence.exposed

import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.datetime.CurrentDateTime
import org.jetbrains.exposed.v1.datetime.datetime

object PublishChannelTable : LongIdTable("publish_channel") {
    val name = varchar("name", 255)
    val accessUrl = text("access_url")
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val updatedAt = datetime("updated_at").defaultExpression(CurrentDateTime)
}