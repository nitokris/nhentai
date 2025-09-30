package com.nitokrisalpha.domain.service

import com.nitokrisalpha.domain.entity.Site
import com.nitokrisalpha.domain.entity.WorkMetaData
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status

class DLSiteProviderImpl(
    private val client: HttpHandler,
    override val site: Site = Site.DLSITE
) : WorkMetaDataProvider {


    companion object {
        private const val DLSITE_URL = "https://www.dlsite.com/maniax/work/=/product_id/RJ01304896.html"
    }

    override fun fetchMetaData(id: String): WorkMetaData {
        val request = Request(Method.GET, DLSITE_URL)
        val response = client(request)
        if (response.status == Status.OK) {
            print(response.bodyString())
        }
        TODO("Not yet implemented")
    }
}