package com.nitokrisalpha.application.adapter.thirdpart

import com.fleeksoft.ksoup.Ksoup
import com.nitokrisalpha.application.configuration.FANZAApiProperties
import com.nitokrisalpha.application.logging.log
import com.nitokrisalpha.business.entity.Circle
import com.nitokrisalpha.business.entity.PublishChannel
import com.nitokrisalpha.business.entity.Work
import com.nitokrisalpha.business.entity.work
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
            // 获取作品详情
            val document = Ksoup.parse(html)
            val productItems = document.select(".m-productList .productList__item")
            if (productItems.isEmpty()) {
                break
            }
            for (productItem in productItems) {
                val work = Work()
                work.cover = productItem.select("img").attr("src")
                val titleElement = productItem.select(".tileListTtl__txt a")
                work.title = titleElement.text()
                // https://www.dmm.co.jp/dc/doujin/-/detail/=/cid=d_632387/
                val href = titleElement.attr("href")
                val matchResult = REGEX.find(href)
                matchResult?.groups?.get(1)?.value?.let {
                    val workChannel = PublishChannel(CHANNEL_NAME, it)
                    work.channels.add(workChannel)
                    result.add(work)
                }
            }
            curPage++
        }
        return result
    }

    override fun workDetail(work: Work): Work {
        val channel = work.channels.find {
            it.name == CHANNEL_NAME
        }
        if (channel == null) {
            return work
        }
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
            return work
        }
        val html = response.bodyString()
        val documents = Ksoup.parse(html)
        return work {
            val titleWrapper = documents.selectFirst(".l-areaProductTitle h1")
            if (titleWrapper != null) {
                titleWrapper.select("span").remove()
                title = titleWrapper.text()
            }
            //
            val detailWrapper = documents.selectFirst(".l-areaVariableBoxWrap")
            detailWrapper?.let {
                val imageWrapper = it.select(".l-areaProductImage")
                imageWrapper.select("img").forEach { img ->
                    val href = img.attr("src")
                    href.let {
                        val url = URI(href).toURL()
                        val fileName = url.file
                        // d_691394jp-001.jpg
                        if (!StringUtils.containsIgnoreCase(fileName, "js")) {
                            images += href
                        }
                    }
                }
                val infoWrapper = it.select(".l-areaProductInfo")
                // todo 遍历所有的信息，转换为对应的类属性
                infoWrapper.select("div.productInformation__item").forEach { item ->
                    val ttl = item.select(".informationList__ttl")
                    val txt = item.select(".informationList__txt")
                    log.info("ttl is:{}", ttl.text())
                    log.info("txt is:{}", txt.text())
                }
                val summaryWrapper = it.select(".l-areaProductSummary")
                summary = summaryWrapper.html()
            }
        }


    }
}