package com.nitokrisalpha.domain.repository

import com.nitokrisalpha.domain.specification.Specification

interface EntityRepository<T, ID> {
    fun findById(id: ID): T?
    fun save(entity: T): ID
    fun delete(entity: T)
    fun find(specification: Specification<T>): Collection<T>
}