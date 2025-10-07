package com.nitokrisalpha.common

import org.http4k.core.Uri
import kotlin.test.Test

class UriTest {

    @Test
    fun testUriDecodePath(){
        val uri = Uri.of("https://doujin-assets.dmm.co.jp/digital/comic/d_159829/d_159829jp-001.jpg")
        println(uri.query)
    }

}