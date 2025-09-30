package com.nitokrisalpha.infranstructure.jdbc.table

import org.jetbrains.exposed.v1.core.dao.id.LongIdTable

object Magnets : LongIdTable() {
    val businessId = char("business_id", 32).uniqueIndex()
}