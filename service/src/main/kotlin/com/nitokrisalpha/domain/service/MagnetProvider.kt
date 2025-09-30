package com.nitokrisalpha.domain.service

import com.nitokrisalpha.common.PageResponse
import com.nitokrisalpha.domain.entity.MagnetMetaData

interface MagnetProvider {

    fun search(query: String, currentPage: Int = 1): PageResponse<MagnetMetaData>

}