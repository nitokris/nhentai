package com.nitokrisalpha.infranstructure.jdbc

import com.nitokrisalpha.domain.entity.Magnet
import com.nitokrisalpha.domain.entity.MagnetId
import com.nitokrisalpha.domain.repository.MagnetRepository
import com.nitokrisalpha.infranstructure.jdbc.table.Magnets
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

    override fun save(magnets: Collection<Magnet>): Collection<MagnetId> = transaction {
        Magnets.batchUpsert(magnets, Magnets.businessId) {
            this[Magnets.businessId] = it.id.value
        }.map { MagnetId(it[Magnets.businessId]) }
    }
}