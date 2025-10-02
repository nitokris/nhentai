package com.nitokrisalpha.infranstructure.jdbc.adapter

import com.nitokrisalpha.domain.specification.Specification
import org.jetbrains.exposed.v1.core.Op

interface SpecificationAdapter<T> : Specification<T> {
    fun toExposed(): Op<Boolean>
}