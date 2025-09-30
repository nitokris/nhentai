package com.nitokrisalpha.domain.service

import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.select.Elements
import com.nitokrisalpha.domain.entity.Site
import com.nitokrisalpha.domain.entity.WorkMetaData
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request

class MelonBooksProvider(
    private val client: HttpHandler,
    override val site: Site = Site.MELON
) : WorkMetaDataProvider {

    companion object {

        const val BASE_URL = "https://www.melonbooks.co.jp"
        const val SEARCH_URL = "$BASE_URL/search/search.php"
        const val WORK_URL = "$BASE_URL/detail/detail.php"

        private fun getRequest(id: String): Request {
            val headers = """
                Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7
                Accept-Encoding: gzip, deflate, br, zstd
                Accept-Language: en,zh-CN;q=0.9,zh;q=0.8
                Cache-Control: max-age=0
                Connection: keep-alive
                Cookie: _rt.uid=10a56a00-1213-11f0-5ba6-7e26eb8b1816; _bdck=BD.o3UYo.po5MDPI.3; _bdnvf=YmQ0X3Ny0cs3Do3M1yvIN/V1CfDUMzQ3MbYwNTY2NTEyMtAzBgA=; AUTH_ADULT=1; ECSESSID=0qevo4g2ftv0lqmia0dtjv5g51; MELONBOOKS_CHECK_NEWS=1; MELONBOOKS_CHECK_NEWS_SP=1; MELONBOOKS_CHECK_SHOP=1; MELONBOOKS_CS_CHECK_NEWS=1; _rt.xd=eef985a4; _bdsid=BD.o3UYo.po5MDPI.1758976323031.3; _bd_prev_page_ex=YmQ0X3Ny0csoKSkoVjV2VDVyA6Ly8nK93NSc/Lyk/PzsYr3kfL2sAqBwQVF+SmlySTGQmZJakpiZo1eQUaBqDBOPz0xRNXYxNrQ0NjUz0TMGAA==; CUSTOMER_READING[0]=3193564; CUSTOMER_READING[1]=3082697; CUSTOMER_READING[2]=2687405
                DNT: 1
                Host: www.melonbooks.co.jp
                Sec-Fetch-Dest: document
                Sec-Fetch-Mode: navigate
                Sec-Fetch-Site: none
                Sec-Fetch-User: ?1
                Upgrade-Insecure-Requests: 1
                User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/140.0.0.0 Safari/537.36
                sec-ch-ua: "Chromium";v="140", "Not=A?Brand";v="24", "Google Chrome";v="140"
                sec-ch-ua-mobile: ?0
                sec-ch-ua-platform: "macOS"
            """.trimIndent().split("\n")
            var result = Request(method = Method.GET, uri = WORK_URL)
                .query("product_id", id)
            for (string in headers) {
                string.split(":", limit = 2).let {
                    if (it.size == 2) {
                        result = result.header(it[0], it[1])
                    }
                }
            }

            return result
        }

        private fun Elements.findInfo(thText: String): String {
            return this.filter { it.select("th").text() == thText }
                .map { it.select("td").text() }
                .firstOrNull() ?: ""
        }
    }


    override fun fetchMetaData(id: String): WorkMetaData {
        val request = getRequest(id)
        val response = client(request)
        if (!response.status.successful) {
            throw RuntimeException("Not found")
        }
        val html = response.bodyString()
        val doc = Ksoup.parse(html)
        val title = doc.select("h1.page-header").text()
        val circle = doc.select("p.author-name").text()
        val description = doc.select("div.item-detail")
            .filter {
                it.select("h3").text().contains("作品詳細")
            }.map { it.select("div p").outerHtml() }.firstOrNull() ?: ""
        val trs = doc.select("div.item-detail tbody tr")
        val author = trs.findInfo("作家名")
        val release = trs.findInfo("発行日")
        val tags = trs.findInfo("ジャンル").split(",")
        val previews = doc.select("div.slider figure img")
            .eachAttr("src")
            .map { if (it.startsWith("http")) it else "https:$it" }
        return WorkMetaData(
            title = title,
            description = description,
            previews = previews,
            releaseDate = release,
            finalUpdateDate = "",
            tags = tags,
            actors = listOf(author),
            circle = circle
        )
    }
}