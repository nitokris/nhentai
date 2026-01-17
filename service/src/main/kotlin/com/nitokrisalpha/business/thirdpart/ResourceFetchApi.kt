package com.nitokrisalpha.business.thirdpart

import com.nitokrisalpha.business.entity.Resource

interface ResourceFetchApi {

    fun fetchResource(resource: Resource)

}