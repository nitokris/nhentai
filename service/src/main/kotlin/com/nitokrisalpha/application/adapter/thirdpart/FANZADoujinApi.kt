package com.nitokrisalpha.application.adapter.thirdpart

import com.fleeksoft.ksoup.Ksoup
import com.nitokrisalpha.application.configuration.FANZAApiProperties
import com.nitokrisalpha.application.logging.log
import com.nitokrisalpha.business.entity.Circle
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
    }

    override fun circleWorks(
        circle: Circle,
        sort: Sort,
        page: Int
    ): List<Work> {

        // todo 这里的channel find设计需要更改
        val channel = circle.channels.find {
            it.name == CHANNEL_NAME
        }

        if (channel == null) {
            return emptyList()
        }

        val request = Request(
            Method.GET,
            " https://www.dmm.co.jp/dc/doujin/-/list/=/article=maker/id=${channel.identifier}/sort=${convertSort(sort)}/page=${page}/"
        )
            .header("Cookies", fanzaApiProperties.cookie)

        val response = client(request)
        // todo 如何错误检测？
        if (!response.status.successful) {
            log.error("failed to get circle:{} works from fanza doujin", circle.name)
            return emptyList()
        }
        // 解析html
        val html = response.bodyString()
        // 获取作品详情
        val document = Ksoup.parse(html)
        TODO("Not yet implemented")
    }

    override fun workDetail(work: Work): Work {
        val channel = work.channels.find {
            it.name == CHANNEL_NAME
        }
        if (channel == null) {
            return work
        }
        val request = Request.Companion(
            Method.GET,
            "https://www.dmm.co.jp/dc/doujin/-/detail/=/cid=${channel.identifier}/"
        )
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
                //todo 这里只需要选取 类似：d_691394jp-001.jpg的字段即可，而不是类似：d_691394js-001.jpg
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