package com.nitokrisalpha.domain.entity

data class Resource(
    val name: String,
    val fileName: String,
    val originalName: String,
) {

    private val _items = mutableSetOf<Resource>()

    val items: Collection<Resource>
        get() = _items.toList()
}