package com.nitokrisalpha.domain.entity

import com.fasterxml.jackson.annotation.JsonProperty
import org.http4k.core.Uri
import java.net.URLDecoder

class MagnetId(
    @get:JsonProperty("id")
    override val value: String
) : Id {

    companion object {

        fun newId(magnet: String): MagnetId {
            return MagnetId(extractInfoHashHex(magnet))
        }

        private fun extractInfoHashHex(magnet: String): String {
            val uri = Uri.of(magnet)
            val query = uri.query

            val params = query.split("&").associate {
                val parts = it.split("=", limit = 2)
                parts[0] to if (parts.size > 1) URLDecoder.decode(parts[1], "UTF-8") else ""
            }

            // v1 hash: xt=urn:btih:<hash>
            params["xt"]?.let { xt ->
                when {
                    xt.startsWith("urn:btih:") -> {
                        val hashStr = xt.removePrefix("urn:btih:")
                        return when (hashStr.length) {
                            40 -> hashStr.uppercase() // Hex
                            32 -> base32ToHexFast(hashStr) // Base32 -> Hex
                            else -> throw IllegalArgumentException("magnet btih url format error:${magnet}")
                        }
                    }

                    xt.startsWith("urn:btmh:") -> { // v2 hash: btmh
                        val hashHex = xt.removePrefix("urn:btmh:")
                        if (hashHex.length >= 2) {
                            val hexPart = hashHex.substring(2) // 去掉前缀 12/20
                            return hexPart.uppercase()
                        }
                        throw IllegalArgumentException("magnet btmh url format error:${magnet}")
                    }

                    else -> throw IllegalArgumentException("unknown magnet format:${magnet}")
                }
            }
            return ""
        }

        // 高性能 Base32 -> Hex
        private fun base32ToHexFast(base32: String): String {
            val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567"
            val output = ByteArray(20) // SHA-1 20 字节
            var buffer = 0
            var bitsLeft = 0
            var index = 0

            for (c in base32.uppercase()) {
                val val5 = chars.indexOf(c)
                require(val5 >= 0) { "Invalid Base32 char: $c" }

                buffer = (buffer shl 5) or val5
                bitsLeft += 5

                if (bitsLeft >= 8) {
                    bitsLeft -= 8
                    output[index++] = ((buffer shr bitsLeft) and 0xFF).toByte()
                }
            }

            // 转 Hex
            return output.joinToString("") { "%02X".format(it) }
        }
    }

}