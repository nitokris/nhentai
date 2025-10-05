package com.nitokrisalpha.infranstructure.jdbc.table

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.Table

object WorkFiles : Table() {
    val workId = char("workId", 32).references(Works.businessId, onDelete = ReferenceOption.CASCADE)
    val hash = char("hash", 128)
    val fileName = text("fileName")
    val displayName = text("displayName")
    val originalPath = text("originalPath")
    val needProcess = bool("needProcess").default(true)
    val processStatus = varchar("processStatus", 255).default("")
    override val primaryKey = PrimaryKey(hash, name = "work_file_hash")

    init {
    }

}