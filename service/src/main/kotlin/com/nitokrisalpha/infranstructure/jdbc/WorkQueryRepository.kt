package com.nitokrisalpha.infranstructure.jdbc

import com.nitokrisalpha.application.dto.WorkDto
import com.nitokrisalpha.application.dto.WorkFileDto
import com.nitokrisalpha.common.PageResponse
import com.nitokrisalpha.domain.entity.MagnetMetaData
import com.nitokrisalpha.domain.entity.WorkId
import com.nitokrisalpha.infranstructure.jdbc.adapter.paginate
import com.nitokrisalpha.infranstructure.jdbc.table.Magnets
import com.nitokrisalpha.infranstructure.jdbc.table.WorkFiles
import com.nitokrisalpha.infranstructure.jdbc.table.WorkMagnets
import com.nitokrisalpha.infranstructure.jdbc.table.Works
import org.jetbrains.exposed.v1.core.JoinType
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.springframework.stereotype.Repository
import kotlin.text.split

@Repository
class WorkQueryRepository {

    fun recent(count: Int): Collection<WorkDto> = transaction {
        Works.selectAll().limit(count).orderBy(Works.id, SortOrder.DESC).map { it ->
            val id = it[Works.businessId]
            val title = it[Works.title]
            val description = it[Works.description]
            val cover = it[Works.previews].split(",").filter { it.isNotEmpty() }[0]
            WorkDto(
                id = id,
                title = title,
                description = description,
                cover = cover
            )
        }
    }

    fun findByPage(page: Int, pageSize: Int): PageResponse<WorkDto> {
        return transaction {
            Works.selectAll()
                .orderBy(column = Works.siteId, order = SortOrder.DESC)
                .paginate(page, pageSize) { it ->
                    val id = it[Works.businessId]
                    val title = it[Works.title]
                    val description = it[Works.description]
                    val cover = it[Works.previews].split(",").filter { it.isNotEmpty() }[0]
                    val tags = it[Works.tags].split(",").filter { it.isNotEmpty() }
                    WorkDto(
                        id = id,
                        title = title,
                        description = description,
                        cover = cover,
                        tags = tags
                    )
                }
        }
    }

    fun findById(workId: WorkId): WorkDto? = transaction {
        Works.selectAll().where { Works.businessId eq workId.value }
            .limit(1)
            .firstOrNull()?.let { it ->
                val id = it[Works.businessId]
                val title = it[Works.title]
                val description = it[Works.description]
                val previews = it[Works.previews].split(",").filter { it.isNotEmpty() }
                val actors = it[Works.actors]
                val tags = it[Works.tags].split(",").filter { it.isNotEmpty() }
                val circle = it[Works.circle]
                val magnets =
                    Magnets.join(WorkMagnets, joinType = JoinType.INNER, Magnets.businessId, WorkMagnets.magnetId)
                        .select(Magnets.columns)
                        .where { WorkMagnets.workId eq workId.value }
                        .map {
                            val title = it[Magnets.title]
                            val url = it[Magnets.url]
                            val size = it[Magnets.size]
                            val date = it[Magnets.date]
                            val category = it[Magnets.category]
                            MagnetMetaData(title, url, size, date, category)
                        }
                val workFiles = WorkFiles.selectAll()
                    .where { WorkFiles.workId eq workId.value }
                    .map {
                        WorkFileDto(it[WorkFiles.displayName], it[WorkFiles.hash])
                    }
                WorkDto(
                    id = id,
                    title = title,
                    description = description,
                    previews = previews,
                    magnets = magnets,
                    actors = actors,
                    tags = tags,
                    circle = circle,
                    files = workFiles
                )
            }

    }

}