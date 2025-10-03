package com.nitokrisalpha.infranstructure.jdbc

import com.nitokrisalpha.domain.entity.Magnet
import com.nitokrisalpha.domain.entity.MagnetId
import com.nitokrisalpha.domain.repository.MagnetRepository
import com.nitokrisalpha.domain.specification.Specification
import com.nitokrisalpha.infranstructure.jdbc.table.Magnets
import org.jetbrains.exposed.v1.jdbc.batchInsert
import org.jetbrains.exposed.v1.jdbc.batchUpsert
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class MagnetRepositoryImpl : MagnetRepository {
    override fun findById(id: MagnetId): Magnet? {
        TODO("Not yet implemented")
    }

    override fun save(entity: Magnet): MagnetId = transaction {
        TODO("Not yet implemented")
    }

    override fun delete(entity: Magnet) {
        TODO("Not yet implemented")
    }

    override fun find(specification: Specification<Magnet>): Collection<Magnet> {
        TODO("Not yet implemented")
    }

    override fun save(magnets: Collection<Magnet>): Collection<MagnetId> = transaction {
        Magnets.batchInsert(magnets, ignore = true) {
            this[Magnets.businessId] = it.id.value
            this[Magnets.title] = it.metadata.title
            this[Magnets.url] = it.metadata.url
            this[Magnets.size] = it.metadata.size
            this[Magnets.date] = it.metadata.date
            this[Magnets.category] = it.metadata.category
        }.map { MagnetId(it[Magnets.businessId]) }
    }
}