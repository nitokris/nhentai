package com.nitokrisalpha.domain.entity

data class MagnetMetaData(
    val title: String,
    val url: String,
    val size: String,
    val date: String,
) {

    init {
        if (!url.startsWith("magnet:")) {
            throw IllegalArgumentException("Magnet link must start with 'magnet:'")
        }
    }

}

