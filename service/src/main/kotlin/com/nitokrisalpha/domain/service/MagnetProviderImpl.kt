package com.nitokrisalpha.domain.service

import com.fleeksoft.ksoup.Ksoup
import com.nitokrisalpha.common.PageResponse
import com.nitokrisalpha.common.Pagination
import com.nitokrisalpha.domain.entity.MagnetMetaData
import org.http4k.core.HttpHandler
import org.http4k.core.Method

class MagnetProviderImpl(
    val client: HttpHandler
) : MagnetProvider {

    companion object {
        private const val BASE_URL = "https://sukebei.nyaa.si/"

        private fun getRequest(query: String, page: Int) =
            org.http4k.core.Request(
                method = Method.GET,
                uri = BASE_URL
            ).query("f", "0")
                .query("c", "1_0")
                .query("q", query)
                .query("p", page.toString())

    }

    override fun search(
        query: String,
        currentPage: Int
    ): PageResponse<MagnetMetaData> {
        val request = getRequest(query, currentPage)
        val response = client(request)
        if (!response.status.successful) {
            return PageResponse(
                data = emptyList(),
                pagination = Pagination(page = 0, size = 0, totalPage = 0, total = 0)
            )
        }
        val body = response.bodyString()
        val document = Ksoup.parse(body)
        val trList = document.select(".table-responsive tbody tr")
        val list = trList.map { item ->
            val title = item.selectFirst("td[colspan=2]")?.text() ?: ""
            val magnet = item.selectFirst("a[href^='magnet']")?.attr("href") ?: ""
            val size = item.select("td")[3].text()
            val date = item.select("td")[4].text()
            MagnetMetaData(title = title, url = magnet, size = size, date = date)
        }
        val totalStr =
            document.select("div.pagination-page-info").text()
        var total = 75
        if (totalStr.isNotBlank()) {
            total = totalStr.substringAfter("out of").substringBefore("results")
                .trim().toInt()
        }
        return PageResponse(
            data = list,
            pagination = Pagination(
                page = currentPage,
                size = list.size,
                totalPage = if (total % 75 == 0) total / 75 else total / 75 + 1,
                total = total
            )
        )
    }
}