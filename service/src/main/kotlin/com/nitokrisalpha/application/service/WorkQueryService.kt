package com.nitokrisalpha.application.service

import com.nitokrisalpha.domain.entity.FileEntity
import com.nitokrisalpha.infranstructure.jdbc.table.FileEntities
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.springframework.stereotype.Service

@Service
class WorkQueryService {


    fun readWork(fileHash: String): Collection<FileEntity> {
        return transaction {
            FileEntities.selectAll()
                .where { FileEntities.fileHash eq fileHash }
                .map {
                    FileEntity(it[FileEntities.fileName], it[FileEntities.originalFileName], it[FileEntities.readOrder])
                }
        }

    }

}