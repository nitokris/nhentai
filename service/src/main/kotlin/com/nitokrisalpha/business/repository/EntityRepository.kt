package com.nitokrisalpha.business.repository

interface EntityRepository<T, ID> {
    fun save(entity: T): T

    fun findById(id: ID): T?
}