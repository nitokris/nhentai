package com.nitokrisalpha.domain.specification

interface Specification<T> {

    fun isSatisfiedBy(candidate: T): Boolean

    infix fun and(other: Specification<T>): Specification<T> {
        return AndSpecification(this, other)
    }

    infix fun or(other: Specification<T>): Specification<T> {
        return OrSpecification(this, other)
    }
}