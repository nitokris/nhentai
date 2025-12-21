package com.nitokrisalpha.business.entity

class Work(
) {
    val channels: MutableList<PublishChannel> = mutableListOf()
    var title: String = ""
    val images = mutableSetOf<String>()
    var summary: String = ""
    override fun toString(): String {
        return "Work(channels=$channels, title='$title', images=$images, summary='$summary')"
    }

}

fun work(block: Work.() -> Unit): Work {
    return Work().apply(block)
}