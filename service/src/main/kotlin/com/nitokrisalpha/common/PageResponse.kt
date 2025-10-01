package com.nitokrisalpha.common

data class PageResponse<T>(
    val data: Collection<T>,
    val pagination: Pagination
)