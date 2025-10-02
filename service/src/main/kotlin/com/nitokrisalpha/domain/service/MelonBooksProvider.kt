package com.nitokrisalpha.domain.service

import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.select.Elements
import com.nitokrisalpha.common.logger
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
                Cookie: _rt.uid=10a56a00-1213-11f0-5ba6-7e26eb8b1816; _bdck=BD.o3UYo.po5MDPI.3; _bdnvf=YmQ0X3Ny0cs3Do3M1yvIN/V1CfDUMzQ3MbYwNTY2NTEyMtAzBgA=; AUTH_ADULT=1; ECSESSID=u5ekvkcqeht7j5lvcn4nteddr0; MELONBOOKS_CHECK_NEWS=1; MELONBOOKS_CHECK_NEWS_SP=1; MELONBOOKS_CHECK_SHOP=1; MELONBOOKS_CS_CHECK_NEWS=1; _rt.xd=eef985a4; _bdsid=BD.o3UYo.po5MDPI.1759405837395.3; _bd_prev_page_ex=YmQ0X3Ny0csoKSkoVjV2VDVyA6Ly8nK93NSc/Lyk/PzsYr3kfL2sAqBwSmpJYmYOnKFXkFGgauxWUJSfUppcEp+ZomrsYmxoaWhpaaBnDAA=; CUSTOMER_READING[0]=3191990; CUSTOMER_READING[1]=3188921; CUSTOMER_READING[2]=3114947; CUSTOMER_READING[3]=2839209; CUSTOMER_READING[4]=3193564; CUSTOMER_READING[5]=2746730; CUSTOMER_READING[6]=3082697; CUSTOMER_READING[7]=2687405
                DNT: 1
                Host: www.melonbooks.co.jp
                Referer: https://www.melonbooks.co.jp/detail/detail.php?product_id=${id}
                Sec-Fetch-Dest: document
                Sec-Fetch-Mode: navigate
                Sec-Fetch-Site: same-origin
                Upgrade-Insecure-Requests: 1
                User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/141.0.0.0 Safari/537.36
                sec-ch-ua: "Google Chrome";v="141", "Not?A_Brand";v="8", "Chromium";v="141"
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
            logger.info("response")
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