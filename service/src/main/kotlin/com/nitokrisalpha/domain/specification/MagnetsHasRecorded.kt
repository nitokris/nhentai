package com.nitokrisalpha.domain.specification

import com.nitokrisalpha.domain.entity.Magnet

class MagnetsHasRecorded(
    private val recorded: Collection<Magnet>
) : Specification<Magnet> {
    override fun isSatisfiedBy(candidate: Magnet): Boolean {
        return recorded.find { magnet -> magnet.metadata.url == candidate.metadata.url } != null
    }

}