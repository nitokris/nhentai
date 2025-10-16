package com.nitokrisalpha.domain.specification

import com.nitokrisalpha.domain.entity.Work



class WorkTypeIsNot(
    private val excludedType: String
) : Specification<Work> {
    companion object {
        operator fun invoke(value: String): WorkTypeIsNot {
            return WorkTypeIsNot(value)
        }
    }

    override fun isSatisfiedBy(candidate: Work): Boolean {
        return candidate.type.trim() != excludedType
    }


}