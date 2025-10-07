package com.nitokrisalpha.domain.entity

data class WorkFile(
    val fileHash: String,
    val fileName: String, // 实际存储的文件名
    val displayName: String, // 显示的名字
    val originalPath: String,
    val originalName: String,
    val entities: Collection<FileEntity>,
)
