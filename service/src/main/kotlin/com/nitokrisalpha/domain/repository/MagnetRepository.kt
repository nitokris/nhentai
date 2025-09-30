package com.nitokrisalpha.domain.repository

import com.nitokrisalpha.domain.entity.Magnet
import com.nitokrisalpha.domain.entity.MagnetId

interface MagnetRepository : EntityRepository<Magnet, MagnetId> {
    fun save(magnets: Collection<Magnet>): Collection<MagnetId>
}