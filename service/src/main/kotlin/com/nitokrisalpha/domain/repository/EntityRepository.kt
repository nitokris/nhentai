package com.nitokrisalpha.domain.repository

interface EntityRepository<T, ID> {
    fun findById(id: ID): T?
    fun save(entity: T): ID
    fun delete(entity: T)
}