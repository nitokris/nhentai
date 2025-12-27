package com.nitokrisalpha.application.adapter.db

import com.nitokrisalpha.application.adapter.db.exposed.WorkTable
import com.nitokrisalpha.business.builder.buildMetadata
import com.nitokrisalpha.business.entity.Work
import com.nitokrisalpha.business.entity.WorkId
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
                it[workId] = entity.id.value
                it[title] = entity.metaData.title
                it[cover] = entity.metaData.cover
                it[format] = entity.metaData.format
                it[series] = entity.metaData.series
                it[summary] = entity.metaData.summary
                it[subjectMatter] = entity.metaData.subjectMatter
                it[images] = entity.metaData.images.joinToString(",")
                it[tags] = entity.metaData.tags.joinToString(",")
            }
        }
        return entity
    }

    override fun findById(id: Long): Work? {
        return transaction {
            WorkTable.selectAll()
                .where { WorkTable.id eq id }
                .firstOrNull()?.let {
                    val id = WorkId(it[WorkTable.workId])
                    val meta = buildMetadata {
                        title(it[WorkTable.title])
                        cover(it[WorkTable.cover])
                        format(it[WorkTable.format])
                        series(it[WorkTable.series])
                        summary(it[WorkTable.summary])
                        subjectMatter(it[WorkTable.subjectMatter])
                        setImages(
                            if (it[WorkTable.images].isBlank()) {
                                emptyList()
                            } else {
                                it[WorkTable.images].split(",")
                            }
                        )
                        setTags(
                            if (it[WorkTable.tags].isBlank()) {
                                emptyList()
                            } else {
                                it[WorkTable.tags].split(",")
                            }
                        )
                    }
                    Work(id = id, metaData = meta)
                }
        }
    }
}