package com.nitokrisalpha.domain.entity

data class WorkMetaData(
    val title: String,
    val description: String,
    val previews: List<String>,
    val releaseDate: String,
    val finalUpdateDate: String,
    val tags: List<String>,
    val actors: List<String>,
    val circle: String,
) {

}