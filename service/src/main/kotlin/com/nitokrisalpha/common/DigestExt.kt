package com.nitokrisalpha.common

import java.io.File
import java.io.FileInputStream
import java.security.MessageDigest

fun File.sha512(): String {
    if (!this.exists()) {
        throw IllegalArgumentException("current file not exists")
    }
    val buffer = ByteArray(1024)
    val md = MessageDigest.getInstance("SHA-512")
    FileInputStream(this).use { fis ->
        var read: Int
        while (fis.read(buffer).also { read = it } != -1) {
            md.update(buffer, 0, read)
        }
    }
    val digest = md.digest()
    return digest.fold("") { str, byte -> str + "%02x".format(byte) }
}