package com.nitokrisalpha.infranstructure.jdbc.table

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable

object WorkFiles : IntIdTable() {
    val workId = char("workId", 32)
    val hash = char("hash", 128)
    val fileName = text("fileName")
    val displayName = text("displayName")
    val originalPath = text("originalPath")
    val needProcess = bool("needProcess").default(true)
    val processStatus = varchar("processStatus", 255).default("")

    init {
        uniqueIndex(workId, hash)
    }

}