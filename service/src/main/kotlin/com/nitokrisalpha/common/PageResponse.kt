package com.nitokrisalpha.common

data class PageResponse<T>(
    val data: Collection<T>,
    val page: Int,
    val size: Int,
    val totalPage: Int,
    val total: Int
)