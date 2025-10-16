package com.nitokrisalpha.domain.service

import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.select.Elements
import com.nitokrisalpha.common.logger
import com.nitokrisalpha.domain.entity.Site
import com.nitokrisalpha.domain.entity.WorkMetaData
import com.nitokrisalpha.infranstructure.config.PathConfig
import org.apache.commons.io.IOUtils
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Uri
import java.io.File
import java.io.FileOutputStream


class FanzaProviderImpl(
    private val client: HttpHandler,
    override val site: Site = Site.FANZA,
    private val pathConfig: PathConfig
) : WorkMetaDataProvider {


    companion object {

        /**
         * request统一创建
         */
        private fun getFanzaRequest(id: String): Request {
            val request = Request(
                Method.GET,
                "https://www.dmm.co.jp/dc/doujin/-/detail/=/cid=${id}/"
            )
                .header("Referer", "http://localhost:63342/")
                .header("accept-encoding", "gzip, deflate, br, zstd")
                .header(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/"
                )
                .header("Sec-Ch-Ua", """"Chromium";v="140", "Not=A?Brand";v="24", "Google Chrome";v="140"""")
                .header("Sec-Ch-Ua-Mobile", "?0")
                .header("Sec-Ch-Ua-Platform", """macOS""")
                .header("Sec-Fetch-Dest", "document")
                .header("Sec-Fetch-Mode", "navigate")
                .header("Fetch-Site", "cross-site")
                .header("Sec-Fetch-User", "?1")
                .header("Upgrade-Insecure-Requests", "1")
                .header(
                    "accept",
                    "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7"
                )
                .header("dnt", "1")
                .header("priority", "u=0,i")
                .header(
                    "Cookie",
                    "XSRF-TOKEN=eyJpdiI6IldEKzY4L3FNdkpPcGtrNWQreFFyOVE9PSIsInZhbHVlIjoiU0Y5Smw5YkQ5dmFzOVRWWXhSVWlsNElTaXdlTnROZU5QdmoybW1pQnhYRW1ZY2t1OG5DNW5qRkk4dHlPZ1c4RkY1V3NZdGs2RDBPK01XdkNZYnl5cXYvZTFzZCtqQndBVHhiaVRab2cyUkRqQm5yYnJVVE1zY01GOGRQMWdaODAiLCJtYWMiOiJjNmYyYjViODNhNzk2YTM1ZDQ3NmVmM2JmYzA4ZWYxMzBiY2JiNjYyNzFkNjg1MjQzMTkwY2E0MmRiYjI5ODQ4IiwidGFnIjoiIn0%3D; ec_session=eyJpdiI6IlZISnpwSHlmQ1NmMlhvMnhwL2dLS1E9PSIsInZhbHVlIjoiNHJRWjgvYVJ5NDhGNGpHVHduVUY2bmhwTVdCbU9MNDI1ajhFRTlBVmhmUFNpRUhYcHM1LzVQQVg3NHJzUC9kNUVubzRqRndFRFRBbTlYNU5EdjNySDVPQVFZRXQ0MXhpRGQ3ZldUQ3JNL0VPQmxVYXR6c01JMTNncFczZWE5Y00iLCJtYWMiOiJkNGM1NDA3MzNjZmQ3NjRmYzA1NGUyZmM4ZDFiZTU3OTI2OWEzMzJhNDQ1ZmE5ZmFlMjk0MmJiNDE4NzJlMDBmIiwidGFnIjoiIn0%3D; Fra4FvqwFtn8Y94ZijkLraIYfkukP4YioJgyDhFZ=eyJpdiI6IlpkNjg3bkd0U2hqY2h2bkc4VzBuUVE9PSIsInZhbHVlIjoiMVhueXFaZ0xnS3pJVk5DbUJjajdWTncvYUpOR0JWb2g4N3VvUUhXV2ZKY1N4UHl2eklUNUpmSFd6dGdBSG1VM01oc0dQQy9OS09ZMGNiSjNqRGlTOW44TTZVU0JMYkYyRkVtMTJlSTRDb1MvRDZHM0RQWlMyK2h2T3Zyc0hUdThDbVp0bmRnOFNsTEhDMy9sblNuNEduK1R6QWsvdHU0eU05Y1ExMkVoN2ZvdUNJUUFNNXRUaU9ZN01nK21nMVNUWG9ESGJUZFpvWGQwV1JQRnV2YWRNRmZHQkFtVlZrME1DK3J1TXpmbUprT09xK0FHaUExMi8rWGJ1c3pVOUVHcVMxK0hGOHVGVDhjODB3VVNSQ1E3c2taSm1zdjllc3Y0MzI5aWtzRkwzdVlJR25zd0VQc1NtOE5tMkJxUUplWExMTk5tVW13TW1HNVV3cUlSWlIzVm0zUmRmZ3Y1V1VPOHBnOU9WY0dsMldlR3ZtOTdkeXF6bUZpaVE1RDBiMUoyY2pvWEhJa1RtN1FscTM2QXkzV1RQcG9oam1NZGRyL0FLOUgxbEViWUJIZ2wwdjZBSmVpSURzSU5ielA1bDRQQUJzbDVoUDA1bEp3ZTFISTQ2U3NyeDU1V0dQYmo0UU8yNkN3VEdTTkJuUlozbzhyWnNuT1FwdmJ4bGVsaURHUFZzS2dVK29kaVNsbHJWY2hjalJnZGVlTWhhaXJSTTVIUllIWWNBcUxVdTVnPSIsIm1hYyI6IjY1ODMyMTA5ODM5ZDI0NjExN2MwNjRlMTkzZjlkMTJlMzAyZDZlNzkxYWM1MTU5ZGQxN2FjNGMwYzc0YTZhNmEiLCJ0YWciOiIifQ%3D%3D; rieSh3Ee_ga=GA1.1.669275324.1741432356; _yjsu_yjad=1741432356.961f8f14-abee-4bd9-b866-07d416ca300c; FPID=FPID2.3.Laz6ngX830Ko3jT%2FMt4Ik%2F%2FUTC23%2BnALmS9vvc7gkGA%3D.1741432356; _im_vid=01JNTQA07WE3E88ETP85KH8QAB; adpf_uid=cSjZsZseZNsfcayt; _tt_enable_cookie=1; _ttp=01JNTQCAA537T997SBSTJMZKQJ_.tt.2; uid=zUgLYPdgZaIaApVwyfAO; suid=zUgLYPdgZaIaApVwyfAO; cklg=ja; cto_bundle=J8FZMl8lMkJTUVpFYWY2bGhnJTJGJTJCM3FJJTJGa3d3cjRtM2ZUT2dnRU1kOTVyWFVYc2RGNDdrd1JBWVlSQzdSdEtxUkdaRURkTXJvWkFkNnBnQktOU1N6QklvSVZlVjFyMzBJVG9WM0tITUolMkJHTXJMMGxuRzdvVGdUaEtaQ0pXVlpaOGJDdmE2cmJJVThHYTVWZDBNNGpZeVN5RktyTnN3JTNEJTNE; rieSh3Ee_ga_KQYE0DE5JW=GS1.1.1743224263.4.0.1743224268.0.0.524567912; check_done_login=true; alcb=true; top_dummy=5ba0f52c-22a0-4322-8b4b-a69f1d360a85; digital[play_volume]=0.5; digital[play_muted]=0; exclude=ai.all; digital[autoplayer_muted]=1; is_internal=true; deviceIdentify=wWq92up1ut4DnluB7zqlAmbqJs5cf5; dc[list]=%7B%22dc_pagelimit%22%3A120%2C%22dc_pageview%22%3A%22package%22%7D; guest_id=DwNSQRwFEgRLVAdJ; dig_history=h_1379jdxa57719%2Ch_610qnbm00049; subscription_members_status=non; cdp_id=iDKHjgbNY5oqeFHR; top_pv_uid=d2b18153-19a9-4c7d-bf5e-14702a76f125; age_check_done=1; ckcy=1; secid=45474e7baf5df5f10a250e086830cd3e; login_secure_id=45474e7baf5df5f10a250e086830cd3e; is_intarnal=true; ixd_lastclick=6828,1758894358"
                )

            return request
        }

        private fun getImgRequest(url: String): Request {
            return Request(Method.GET, url)
        }

        private fun Elements.findInfo(infoColumn: String): String {
            val item = this.filter({ it.select(".informationList__ttl").text() == infoColumn })
            if (item.isNotEmpty()) {
                return item[0].select(".informationList__txt").text()
            }
            return ""
        }

        private fun Elements.findElements(infoColumn: String, infoValue: String): Elements {
            val item = this.filter { it.select(".informationList__ttl").text() == infoColumn }
            if (item.isNotEmpty()) {
                return item[0].select(infoValue)
            }
            return Elements()
        }

        private fun Elements.ownText(): String {
            return this.joinToString(" ") { it.ownText() }.trim()
        }

        private fun Uri.getFileName(): String {
            return this.path.substringAfterLast("/")
        }
    }

    override fun fetchMetaData(id: String): WorkMetaData {
        val request = getFanzaRequest(id)
        val response = client(request)
        if (response.status.successful) {
            val html = response.bodyString()
            val doc = Ksoup.parse(html = html)
            val title = doc.selectFirst(".productTitle__txt")?.ownText() as String
            val circle = doc.select(".circleName__item").text()
            val description = doc.select("div.summary__item.summary__item--trailer").html()
            val previews = doc.select("ul.productPreview img").eachAttr("src")
            val items = doc.select(".m-productInformation .productInformation__item")
            val releaseDate = items.findInfo("配信開始日")
            val author = items.findInfo("作者")
            val finalUpdateDate = items.findInfo("最終更新日")
            val workType = items.findInfo("作品形式")
            val tagHtmlTags = items.findElements("ジャンル", "a.genreTag__txt")
            val tags = tagHtmlTags.eachText()
            val mapping = downloadAndReplacePreviews(previews)
            val replacedPaths = mutableListOf<String>()
            for (str in previews) {
                replacedPaths += if (mapping.containsKey(str)) {
                    mapping[str] as String
                } else {
                    str
                }
            }
            return WorkMetaData(
                title = title,
                description = description,
                previews = replacedPaths,
                releaseDate = releaseDate,
                finalUpdateDate = finalUpdateDate,
                tags = tags,
                actors = listOf(author),
                circle = circle,
                workType = workType
            )
        }
        logger.error("failed to fetch metadata from fanza for id: $id,http status: ${response.status}")
        throw RuntimeException("failed to fetch metadata from fanza for id: $id")
    }

    private fun downloadAndReplacePreviews(previews: List<String>): Map<String, String> {
        val result = mutableMapOf<String, String>()
        val rootPath = pathConfig.previews.replace("/app", "")
        for (preview in previews) {
            val fileName = Uri.of(preview).getFileName()
            val request = getImgRequest(preview)
            val resp = client(request)
            resp.use { it ->
                if (it.status.successful) {
                    val file = File(pathConfig.previews, fileName)
                    file.createNewFile()
                    FileOutputStream(file).use { out ->
                        IOUtils.copy(it.body.stream, out)
                        result[preview] = "${rootPath}/${fileName}"
                    }
                }
            }
        }
        return result
    }

}