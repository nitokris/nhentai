package com.nitokrisalpha.domain.repository

import com.nitokrisalpha.domain.entity.SiteInfo
import com.nitokrisalpha.domain.entity.Work
import com.nitokrisalpha.domain.entity.WorkId

interface WorkRepository : EntityRepository<Work, WorkId> {
    fun findBySiteInfo(siteInfo: SiteInfo): Work?
}