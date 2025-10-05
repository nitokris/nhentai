package com.nitokrisalpha.infranstructure.jdbc.table

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable

object FileEntities : LongIdTable() {
    val fileHash = char("hash", 128).references(ref = WorkFiles.hash, onDelete = ReferenceOption.CASCADE)
    val fileName = varchar("fileName", 512).uniqueIndex()
    val originalFileName = varchar("originalFileName", 512)
    val readOrder = integer("readOrder")
}