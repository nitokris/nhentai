package com.nitokrisalpha.infranstructure.jdbc.table

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable

object WorkMagnets : LongIdTable() {
    val workId = char("work_id", 32).references(ref = Works.businessId, onDelete = ReferenceOption.CASCADE)
    val magnetId = char("magnet_id", 40).references(ref = Magnets.businessId, onDelete = ReferenceOption.CASCADE)

    init {
        uniqueIndex(customIndexName = "work_magnets", workId, magnetId)
    }
}