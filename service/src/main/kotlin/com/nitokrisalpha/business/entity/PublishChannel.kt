package com.nitokrisalpha.business.entity

class PublishChannel(
    val accessUrl: String,
    val name: String
) : BaseEntity() {

    fun match(url: String): Boolean {
        return url.startsWith(accessUrl)
    }

}