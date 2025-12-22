package com.nitokrisalpha.application.adapter.thirdpart

import com.fleeksoft.ksoup.Ksoup
import com.nitokrisalpha.application.configuration.SukebeiApiProperties
import com.nitokrisalpha.business.entity.Work
import com.nitokrisalpha.business.thirdpart.ResourceSearchApi
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.springframework.stereotype.Component

@Component
class SukebeiApi(
    private val client: HttpHandler,
    private val properties: SukebeiApiProperties,
) : ResourceSearchApi {

    companion object {
        private const val SUKEBEI_SEARCH_URL = "https://sukebei.nyaa.si/"
    }

    override fun searchResource(work: Work) {
        val title = work.title
        val request = Request(Method.GET, SUKEBEI_SEARCH_URL)
            .query("f", "0")
            .query("c", "1_0")
            .query("q", title)
        val response = client(request)
        if (!response.status.successful) {
            return
        }
        val html = response.bodyString()
        val document = Ksoup.parse(html)

        TODO("Not yet implemented")
    }
}