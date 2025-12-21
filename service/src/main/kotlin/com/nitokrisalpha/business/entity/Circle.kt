package com.nitokrisalpha.business.entity


class Circle(
    val name: String
) {
    val channels: MutableList<PublishChannel> = mutableListOf()
}