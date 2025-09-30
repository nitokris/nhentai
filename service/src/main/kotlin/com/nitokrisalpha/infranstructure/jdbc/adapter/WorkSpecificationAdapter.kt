package com.nitokrisalpha.infranstructure.jdbc.adapter

import com.nitokrisalpha.domain.entity.Work
import com.nitokrisalpha.domain.specification.Specification
import org.jetbrains.exposed.v1.core.Op

class WorkSpecificationAdapter(
    val specification: Specification<Work>
) {
    fun toExposed(): Op<Boolean> {
        return when(specification){
            else -> throw IllegalArgumentException("Unknown specification type: ${specification::class}")
        }
    }
}