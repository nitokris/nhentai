package com.nitokrisalpha.infranstructure.metadata

import com.nitokrisalpha.domain.entity.Site
import com.nitokrisalpha.domain.service.WorkMetaDataProvider
import org.springframework.context.annotation.Configuration

@Configuration
class WorkMetadataProviderFactory(
    private val providers: List<WorkMetaDataProvider>
) {
    private val map: Map<Site, WorkMetaDataProvider> = providers.associateBy { it.site }

    fun getProvider(site: Site): WorkMetaDataProvider? = map[site]
}