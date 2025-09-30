package com.nitokrisalpha.infranstructure.jdbc.table

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.datetime.datetime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


object Works : LongIdTable() {
    val businessId = char("business_id", 32).uniqueIndex("idx_business_id")
    val title = varchar("title", 255)
    val description = text("description").default("")
    val previews = text("previews").default("")
    val releaseDate = varchar("release_date", 50).default("")
    val finalUpdateDate = varchar("final_update_date", 50).default("")
    val tags = text("tags").default("")
    val actors = text("actors").default("")
    val circle = varchar("circle", 255).default("")
    val site = varchar("site", 50).default("UNKNOWN")
    val siteId = varchar("site_id", 255).default("")
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val updatedAt = datetime("updated_at").default(LocalDateTime.now())

    init {
        uniqueIndex(businessId, site, siteId)
        uniqueIndex(site, siteId)
    }
}

@OptIn(ExperimentalTime::class)
private fun LocalDateTime.Companion.now(
    timeZone: TimeZone = TimeZone.currentSystemDefault()
): LocalDateTime {
    return Clock.System.now().toLocalDateTime(timeZone)
}