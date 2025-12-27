package com.nitokrisalpha.application.adapter.db

import com.nitokrisalpha.application.adapter.db.exposed.WorkTable
import com.nitokrisalpha.business.entity.Work
import com.nitokrisalpha.business.repository.WorkRepository
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class WorkRepositoryDB : WorkRepository {
    override fun save(entity: Work): Work {
        transaction {
            WorkTable.insert {
                it[title] = entity.title
                it[cover] = entity.cover
                it[format] = entity.format
                it[series] = entity.series
                it[summary] = entity.summary
                it[subjectMatter] = entity.subjectMatter
                it[images] = entity.images.joinToString(",")
                it[tags] = entity.tags.joinToString(",")
            }
        }
        return entity
    }

    override fun findById(id: Long): Work? {
        return transaction {
            WorkTable.selectAll()
                .where { WorkTable.id eq id }
                .firstOrNull()?.let {
                    Work(
//                    id = it[WorkTable.id].value,
                        title = it[WorkTable.title],
                        cover = it[WorkTable.cover],
                        format = it[WorkTable.format],
                        series = it[WorkTable.series],
                        summary = it[WorkTable.summary],
                        subjectMatter = it[WorkTable.subjectMatter],
                        images = if (it[WorkTable.images].isBlank()) emptySet() else it[WorkTable.images].split(",")
                            .toSet(),
                        tags = if (it[WorkTable.tags].isBlank()) emptySet() else it[WorkTable.tags].split(",").toSet()
                    )
                }
        }
    }
}