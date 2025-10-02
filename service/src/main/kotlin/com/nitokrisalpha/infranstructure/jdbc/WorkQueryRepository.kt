package com.nitokrisalpha.infranstructure.jdbc

import com.nitokrisalpha.application.dto.WorkDto
import com.nitokrisalpha.infranstructure.jdbc.table.Works
import org.jetbrains.exposed.v1.core.SortOrder
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

}