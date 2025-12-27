package com.nitokrisalpha.application.adapter.db.exposed

import org.jetbrains.exposed.v1.core.dao.id.LongIdTable

object WorkTable : LongIdTable("work") {
    val title = varchar("title", 255)
    val cover = varchar("cover", 255).default("")
    val format = varchar("format", 100)
    val series = varchar("series", 100).default("")
    val summary = text("summary")
    val subjectMatter = varchar("subject_matter", 100).default("")
    val images = text("images").default("") // Store as a comma-separated string
    val tags = text("tags").default("") // Store as a comma-separated string
}