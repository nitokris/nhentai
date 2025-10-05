package com.nitokrisalpha.infranstructure.jdbc

import com.nitokrisalpha.domain.entity.*
import com.nitokrisalpha.domain.repository.WorkRepository
import com.nitokrisalpha.domain.specification.Specification
import com.nitokrisalpha.infranstructure.jdbc.table.FileEntities
import com.nitokrisalpha.infranstructure.jdbc.table.WorkFiles
import com.nitokrisalpha.infranstructure.jdbc.table.WorkMagnets
import com.nitokrisalpha.infranstructure.jdbc.table.Works
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.notInList
import org.jetbrains.exposed.v1.jdbc.batchInsert
import org.jetbrains.exposed.v1.jdbc.batchUpsert
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.upsert
import org.springframework.stereotype.Component

@Component
class WorkRepositoryImpl : WorkRepository {
    override fun save(entity: Work) = transaction {
        val id = Works.upsert(Works.businessId, Works.site, Works.siteId) {
            it[businessId] = entity.id.value
            it[title] = entity.metaData.title
            it[description] = entity.metaData.description
            it[previews] = entity.metaData.previews.joinToString(",")
            it[releaseDate] = entity.metaData.releaseDate
            it[finalUpdateDate] = entity.metaData.finalUpdateDate
            it[tags] = entity.metaData.tags.joinToString(",")
            it[actors] = entity.metaData.actors.joinToString(",")
            it[circle] = entity.metaData.circle
            it[site] = entity.siteInfo.site.name
            it[siteId] = entity.siteInfo.id
        }[Works.businessId]
        // 先移除
        WorkMagnets.deleteWhere {
            WorkMagnets.workId eq entity.id.value and (WorkMagnets.magnetId notInList entity.magnets.map { it.value })
        }
        if (entity.magnets.isNotEmpty()) {
            // 再添加
            WorkMagnets.batchInsert(data = entity.magnets, ignore = true) {
                this[WorkMagnets.workId] = entity.id.value
                this[WorkMagnets.magnetId] = it.value
            }
        }
        if (entity.files.isNotEmpty()) {
            WorkFiles.batchInsert(entity.files, ignore = true) {
                this[WorkFiles.workId] = entity.id.value
                this[WorkFiles.hash] = it.fileHash
                this[WorkFiles.fileName] = it.fileName
                this[WorkFiles.displayName] = it.displayName
                this[WorkFiles.originalPath] = it.originalPath
            }
            for (file in entity.files) {
                if (file.entities.isNotEmpty()) {
                    FileEntities.batchInsert(file.entities, ignore = true) {
                        this[FileEntities.fileHash] = file.fileHash
                        this[FileEntities.fileName] = it.fileName
                        this[FileEntities.originalFileName] = it.originalName
                        this[FileEntities.readOrder] = it.readOrder
                    }
                }
            }
        }
        WorkId(id)
    }

    override fun delete(entity: Work) {
        transaction {
            Works.deleteWhere {
                Works.businessId eq entity.id.value
            }
        }
    }

    override fun find(specification: Specification<Work>): Collection<Work> {
        TODO("Not yet implemented")
    }

    override fun findById(id: WorkId): Work? = transaction {
        Works.selectAll().where { Works.businessId eq id.value }.firstOrNull()?.let { it ->
            val title = it[Works.title]
            val description = it[Works.description]
            val previews = it[Works.previews].split(",").filter { it.isNotEmpty() }
            val releaseDate = it[Works.releaseDate]
            val finalUpdateDate = it[Works.finalUpdateDate]
            val tags = it[Works.tags].split(",").filter { it.isNotEmpty() }
            val actors = it[Works.actors].split(",").filter { it.isNotEmpty() }
            val circle = it[Works.circle]
            val magnets = WorkMagnets.selectAll().where { WorkMagnets.workId eq id.value }
                .map { MagnetId(it[WorkMagnets.magnetId]) }
            val siteInfo = SiteInfo(
                site = enumValues<Site>().firstOrNull { site ->
                    site.name == it[Works.site]
                } ?: Site.UNKNOWN,
                id = it[Works.siteId]
            )
            val workMetaData = WorkMetaData(
                title = title,
                description = description,
                previews = previews,
                releaseDate = releaseDate,
                finalUpdateDate = finalUpdateDate,
                tags = tags,
                actors = actors,
                circle = circle
            )
            Work(
                id = WorkId(it[Works.businessId]),
                metaData = workMetaData,
                magnets = magnets,
                siteInfo = siteInfo
            )
        }
    }

    override fun findBySiteInfo(siteInfo: SiteInfo): Work? = transaction {
        Works.selectAll().where { Works.site eq siteInfo.site.name and (Works.siteId eq siteInfo.id) }.firstOrNull()
            ?.let { it ->
                val title = it[Works.title]
                val description = it[Works.description]
                val previews = it[Works.previews].split(",").filter { it.isNotEmpty() }
                val releaseDate = it[Works.releaseDate]
                val finalUpdateDate = it[Works.finalUpdateDate]
                val tags = it[Works.tags].split(",").filter { it.isNotEmpty() }
                val actors = it[Works.actors].split(",").filter { it.isNotEmpty() }
                val circle = it[Works.circle]
                val magnets = WorkMagnets.selectAll().where { WorkMagnets.workId eq it[Works.businessId] }
                    .map { MagnetId(it[WorkMagnets.magnetId]) }
                val siteInfo = SiteInfo(
                    site = enumValues<Site>().firstOrNull { site ->
                        site.name == it[Works.site]
                    } ?: Site.UNKNOWN,
                    id = it[Works.siteId]
                )
                val workMetaData = WorkMetaData(
                    title = title,
                    description = description,
                    previews = previews,
                    releaseDate = releaseDate,
                    finalUpdateDate = finalUpdateDate,
                    tags = tags,
                    actors = actors,
                    circle = circle
                )
                Work(
                    id = WorkId(it[Works.businessId]),
                    metaData = workMetaData,
                    magnets = magnets,
                    siteInfo = siteInfo
                )
            }
    }
}