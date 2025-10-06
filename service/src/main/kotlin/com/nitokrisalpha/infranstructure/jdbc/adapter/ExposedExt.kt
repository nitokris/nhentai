package com.nitokrisalpha.infranstructure.jdbc.adapter

import com.nitokrisalpha.common.PageResponse
import com.nitokrisalpha.common.Pagination
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.jdbc.Query

fun <T> Query.paginate(page: Int, size: Int, mapper: (ResultRow) -> T): PageResponse<T> {
    val total = this.count().toInt()  // Exposed 扩展方法
    val totalPage = (total / size) + 1
    val offset = ((page - 1) * size)
    val list = this.limit(size).offset(offset.toLong()).map(mapper)
    val pagination = Pagination(page = page, size = list.size, total = total, totalPage = totalPage)
    return PageResponse(list, pagination)
}
