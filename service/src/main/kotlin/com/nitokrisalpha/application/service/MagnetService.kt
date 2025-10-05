package com.nitokrisalpha.application.service

import com.nitokrisalpha.application.command.MagnetRecordCommand
import com.nitokrisalpha.common.PageResponse
import com.nitokrisalpha.common.newId
import com.nitokrisalpha.domain.entity.Magnet
import com.nitokrisalpha.domain.entity.MagnetId
import com.nitokrisalpha.domain.entity.MagnetMetaData
import com.nitokrisalpha.domain.repository.MagnetRepository
import com.nitokrisalpha.domain.service.MagnetProvider
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MagnetService(
    val magnetRepository: MagnetRepository,
    val magnetProvider: MagnetProvider
) {
    @Transactional(rollbackFor = [Exception::class])
    fun recordMagnets(command: MagnetRecordCommand): Collection<MagnetId> {

        val magnets = command.magnetMetaData.map {
            Magnet(newId(), it)
        }
        return magnetRepository.save(magnets = magnets)
    }

    fun searchMagnets(query: String, page: Int): PageResponse<MagnetMetaData> {
        return magnetProvider.search(query = query, currentPage = page)
    }

}