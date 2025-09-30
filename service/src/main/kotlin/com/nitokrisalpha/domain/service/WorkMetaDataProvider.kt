package com.nitokrisalpha.domain.service

import com.nitokrisalpha.domain.entity.Site
import com.nitokrisalpha.domain.entity.WorkMetaData

interface WorkMetaDataProvider {

    val site: Site


    fun fetchMetaData(id: String): WorkMetaData

}