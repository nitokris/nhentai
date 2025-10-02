package com.nitokrisalpha.application.service

import com.nitokrisalpha.application.dto.WorkDto
import com.nitokrisalpha.common.newId
import com.nitokrisalpha.domain.entity.*
import com.nitokrisalpha.domain.repository.WorkRepository
import com.nitokrisalpha.domain.service.WorkMetaDataProvider
import com.nitokrisalpha.domain.specification.Specification
import com.nitokrisalpha.infranstructure.jdbc.WorkQueryRepository
import com.nitokrisalpha.infranstructure.metadata.WorkMetadataProviderFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class WorkService(
    private val workRepository: WorkRepository,
    private val workMetadataProviderFactory: WorkMetadataProviderFactory,
    private val workQueryRepository: WorkQueryRepository
) {
    @Transactional(rollbackFor = [Exception::class])
    fun saveNewWork(id: String, site: Site): WorkId {
        val workMetadataProvider: WorkMetaDataProvider = workMetadataProviderFactory.getProvider(site)
            ?: throw IllegalArgumentException("No metadata provider found for site: $site")
        val metaData = workMetadataProvider.fetchMetaData(id)
        val siteInfo = SiteInfo(site, id)
        var work = workRepository.findBySiteInfo(siteInfo)
        if (work != null) {
            work.changeMetaData(metaData)
            workRepository.save(work)
            return work.id
        }
        work = Work(
            id = newId(),
            metaData = metaData,
            siteInfo = siteInfo
        )
        return workRepository.save(work)
    }

    fun findOne(id: String): Work? {
        return workRepository.findById(WorkId(id))
    }

    @Transactional(rollbackFor = [Exception::class])
    fun addMagnets(id: String, magnets: Collection<String>) {
        val work = workRepository.findById(WorkId(id)) ?: throw IllegalArgumentException("Work with id $id not found")
        val magnets = magnets.map { MagnetId(it) }
        work.addNewMagnets(magnets)
        workRepository.save(work)
    }

    fun removeMagnets(id: String, magnets: Collection<String>) {
        val work = workRepository.findById(WorkId(id)) ?: throw IllegalArgumentException("Work with id $id not found")
        val magnets = magnets.map { MagnetId(it) }
        work.removeMagnets(magnets)
        workRepository.save(work)
    }

    fun dropWork(id: String) {
        val work = workRepository.findById(WorkId(id)) ?: throw IllegalArgumentException("Work with id $id not found")
        workRepository.delete(work)
    }

    fun recent(): Collection<WorkDto> {
        return workQueryRepository.recent(10)
    }
}