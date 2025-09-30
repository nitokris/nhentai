package com.nitokrisalpha.domain.entity

data class MagnetMetaData(
        val link: String,
    val name: String,
    val size: String,
    val date: String,
) {

    init {
        if (!link.startsWith("magnet:")) {
            throw IllegalArgumentException("Magnet link must start with 'magnet:'")
        }
    }

}

