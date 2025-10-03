package com.nitokrisalpha.infranstructure.jdbc.table

import org.jetbrains.exposed.v1.core.dao.id.LongIdTable

object Magnets : LongIdTable() {
    val businessId = char("business_id", 32).uniqueIndex()
    val title = text("title").default("")
    val url = text("url").uniqueIndex()
    val size = varchar("size", 255).default("")
    val date = varchar("date", 255).default("")
    val category = text("category").default("")
}