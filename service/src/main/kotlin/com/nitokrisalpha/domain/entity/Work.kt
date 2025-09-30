package com.nitokrisalpha.domain.entity

import com.fasterxml.jackson.annotation.JsonUnwrapped

class Work(
    @get:JsonUnwrapped
    val id: WorkId,
    metaData: WorkMetaData,
    resources: Collection<Resource> = emptyList(),
    magnets: Collection<MagnetId> = emptyList(),
    val siteInfo: SiteInfo = SiteInfo(Site.UNKNOWN, "")
) {
    private val _magnets: MutableCollection<MagnetId> = mutableSetOf()
    private val _resources: MutableCollection<Resource> = mutableSetOf()
    private var _metaData: WorkMetaData = metaData

    init {
        _resources += resources
        _magnets += magnets
    }

    val metaData: WorkMetaData
        get() = _metaData

    val magnets: Collection<MagnetId>
        get() = _magnets.toList()

    val resource: Collection<Resource>
        get() = _resources.toList()


    fun addNewMagnet(magnet: MagnetId) {
        _magnets += magnet
    }

    fun addNewMagnets(magnets: Collection<MagnetId>) {
        _magnets += magnets
    }

    fun addResource(resource: Resource) {
        _resources += resource
    }

    fun addResources(resources: Collection<Resource>) {
        _resources += resources
    }

    fun removeMagnets(magnets: Collection<MagnetId>) {
        _magnets -= magnets.toSet()
    }

    fun changeMetaData(metaData: WorkMetaData) {
        _metaData = metaData
    }
}