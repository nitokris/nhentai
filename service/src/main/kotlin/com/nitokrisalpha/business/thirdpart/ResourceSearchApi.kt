package com.nitokrisalpha.business.thirdpart

import com.nitokrisalpha.business.entity.Work
import com.nitokrisalpha.business.values.SearchResult

interface ResourceSearchApi {

    fun searchResource(work: Work): Collection<SearchResult>

}