package com.nitokrisalpha.infranstructure.jdbc.adapter

import com.nitokrisalpha.common.PageResponse
import com.nitokrisalpha.common.Pagination
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.jdbc.Query

fun <T> Query.paginate(page: Int, size: Int, mapper: (ResultRow) -> T): PageResponse<T> {
    require(size > 0) { "size must be > 0" }

    val total = this.count().toInt() // 你的 count() 扩展
    // 向上取整计算总页数；当 total==0 时，totalPage=0
    val totalPage = if (total == 0) 0 else (total + size - 1) / size

    // 将 requested page 限制到合法范围：至少 1，最多 totalPage（若 totalPage==0，则保持 page = 1）
    val currentPage = when {
        totalPage == 0 -> 1
        page < 1 -> 1
        page > totalPage -> totalPage
        else -> page
    }

    val offset = ((currentPage - 1) * size).toLong()
    val list = if (total == 0) {
        emptyList()
    } else {
        // 使用 limit(size, offset) 更清晰
        this.limit(size).offset(offset).map(mapper)
    }

    val pagination = Pagination(
        page = currentPage,
        size = list.size,
        total = total,
        totalPage = totalPage
    )

    return PageResponse(list, pagination)
}
