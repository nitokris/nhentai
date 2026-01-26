package com.nitokrisalpha.adapter.persistence

import com.nitokrisalpha.business.entity.Circle
import com.nitokrisalpha.business.repository.CircleRepo
import org.springframework.stereotype.Repository

@Repository
class CircleRepoImpl: CircleRepo {
    override fun save(circle: Circle) {
        TODO("Not yet implemented")
    }
}