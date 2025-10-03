package com.nitokrisalpha.application.dto

import com.nitokrisalpha.domain.entity.MagnetMetaData

data class WorkDto(
    val id: String,
    val title: String,
    val description: String,
    val cover: String? = null,
    val magnets: Collection<MagnetMetaData>? = null,
    val previews: Collection<String>? = null
) {
}