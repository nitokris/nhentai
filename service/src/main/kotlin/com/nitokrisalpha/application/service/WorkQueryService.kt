package com.nitokrisalpha.application.service

import com.nitokrisalpha.application.dto.WorkDto
import com.nitokrisalpha.common.PageResponse
import com.nitokrisalpha.domain.entity.FileEntity
import com.nitokrisalpha.infranstructure.jdbc.WorkQueryRepository
import com.nitokrisalpha.infranstructure.jdbc.table.FileEntities
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.springframework.stereotype.Service

@Service
class WorkQueryService(
    private val workQueryRepository: WorkQueryRepository
) {


    fun readWork(fileHash: String): Collection<FileEntity> {
        return transaction {
            FileEntities.selectAll()
                .where { FileEntities.fileHash eq fileHash }
                .map {
                    FileEntity(it[FileEntities.fileName], it[FileEntities.originalFileName], it[FileEntities.readOrder])
                }
        }

    }

    fun page(page: Int, pageSize: Int): PageResponse<WorkDto> {
        return workQueryRepository.findByPage(page, pageSize)
    }

}