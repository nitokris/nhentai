package com.nitokrisalpha.application.adapter.thirdpart

import com.fleeksoft.ksoup.Ksoup
import com.nitokrisalpha.application.configuration.FANZAApiProperties
import com.nitokrisalpha.application.logging.log
import com.nitokrisalpha.business.builder.buildMetadata
import com.nitokrisalpha.business.entity.Circle
import com.nitokrisalpha.business.entity.PublishChannel
import com.nitokrisalpha.business.entity.Work
import com.nitokrisalpha.business.entity.WorkId
import com.nitokrisalpha.business.thirdpart.PublishChannelApi
import com.nitokrisalpha.business.thirdpart.Sort
import org.apache.commons.lang3.StringUtils
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.springframework.stereotype.Component
import java.net.URI

@Component
class FANZADoujinApi(
    val client: HttpHandler,
    val fanzaApiProperties: FANZAApiProperties
) : PublishChannelApi {

    companion object {
        const val CHANNEL_NAME = "FANZA_DOUJIN"

        fun convertSort(sort: Sort): String {
            when (sort) {
                Sort.DATE -> return "date"
            }
        }

        val REGEX = """cid=(d_\d+)""".toRegex()
    }

    override fun circleWorks(circle: Circle, sort: Sort): List<Work> {

        // todo 这里的channel find设计需要更改
        val channel = circle.channels.find {
            it.name == CHANNEL_NAME
        }

        if (channel == null) {
            return emptyList()
        }
        val result = mutableListOf<Work>()
        var curPage = 1
        while (true) {
            val url = buildString {
                append("https://www.dmm.co.jp/dc/doujin/-/list/=/")
                append("article=maker/")
                append("id=${channel.identifier}/")
                append("sort=${convertSort(sort)}/")
                // 动态处理分页逻辑
                if (curPage > 1) {
                    append("page=$curPage/")
                }
            }
            val request = Request(Method.GET, url)
                .header("cookie", fanzaApiProperties.cookie)
                .header("user-agent", fanzaApiProperties.userAgent)
                .header("dnt", fanzaApiProperties.dnt)

            val response = client(request)
            if (!response.status.successful) {
                log.error("failed to get circle:{} works from fanza doujin", circle.name)
                break
            }
            // 解析html
            val html = response.bodyString()
            // 获取概要信息
            val document = Ksoup.parse(html)
            val productItems = document.select(".m-productList .productList__item")
            if (productItems.isEmpty()) {
                break
            }
            for (productItem in productItems) {
                val cover = productItem.select("img").attr("src")
                val titleElement = productItem.select(".tileListTtl__txt a")
                val title = titleElement.text()
                val workMetadata = buildMetadata {
                    this.title = title
                    this.cover = cover
                }
                val work = Work(WorkId(), workMetadata)
                // https://www.dmm.co.jp/dc/doujin/-/detail/=/cid=d_632387/
                val href = titleElement.attr("href")
                val matchResult = REGEX.find(href)
                matchResult?.groups?.get(1)?.value?.let {
                    result.add(work)
                }
            }
            curPage++
        }
        return result
    }

    override fun workDetail(channel: PublishChannel): Work {
        val url = buildString {
            append("https://www.dmm.co.jp/dc/doujin/-/detail/=")
            append("/cid=${channel.identifier}/")
        }
        val request = Request(Method.GET, url)
            .header("cookie", fanzaApiProperties.cookie)
            .header("user-agent", fanzaApiProperties.userAgent)
            .header("dnt", fanzaApiProperties.dnt)
        val response = client(request)
        if (!response.status.successful) {
            log.error("failed to get work detail page:{}", response.status.description)
            return Work.DUMMY
        }
        val html = response.bodyString()
        val documents = Ksoup.parse(html)
        val workMetadata = buildMetadata {
            // 解析标题
            documents.selectFirst(".l-areaProductTitle h1")?.let {
                it.select("span").remove()
                this.title = it.text()
            }
            documents.selectFirst(".l-areaVariableBoxWrap")?.let {
                // 解析图片
                it.select(".l-areaProductImage").select("img").forEach { img ->
                    val href = img.attr("src")
                    href.let {
                        val url = URI(href).toURL()
                        val fileName = url.file
                        // d_691394jp-001.jpg
                        if (!StringUtils.containsIgnoreCase(fileName, "js")) {
                            this.addImage(href)
                        }
                    }
                }
                // 解析信息
                it.select(".l-areaProductInfo").select("div.productInformation__item").forEach { item ->
                    val ttl = item.select(".informationList__ttl").text()
                    val txt = item.select(".informationList__txt")
                    if (ttl == "作品形式") {
                        this.format = txt.text()
                    }
                    if (ttl == "シリーズ") {
                        this.series = txt.text()
                    }
                    if (ttl == "題材") {
                        this.subjectMatter = txt.text()
                    }
                    if (ttl == "ジャンル") {
                        item.select(".informationList__item a").map { tagElement ->
                            addTag(tagElement.text())
                        }
                    }
                    summary = it.select(".l-areaProductSummary").html()
                }
            }
        }
        return Work(WorkId(), workMetadata)
    }
}