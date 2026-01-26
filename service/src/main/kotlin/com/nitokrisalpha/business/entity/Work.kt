package com.nitokrisalpha.business.entity

class Work(
    val title: String,
    val cover: String,
    private var _status: Status = Status.WAIT_SEARCH_RESOURCE,
    val tags: List<String>,
    val description: String,
    val serialize: String,
    val type: Type,

) {
    val status: Status
        get() = _status

    enum class Status(
        override val codeValue: String,
        override val codeName: String,
    ) : BaseStatus {
        WAIT_SEARCH_RESOURCE("WAIT_SEARCH_RESOURCE", "等待搜索资源"),
        RESOURCE_SEARCHED("RESOURCE_SEARCHED", "资源已搜索"),

    }
}