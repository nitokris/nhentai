package com.nitokrisalpha.business.repository

import com.nitokrisalpha.business.entity.Circle

interface CircleRepo {

    fun save(circle: Circle)

}