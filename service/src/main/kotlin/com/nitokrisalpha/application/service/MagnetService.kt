package com.nitokrisalpha.application.service

import com.nitokrisalpha.application.command.MagnetRecordCommand
import com.nitokrisalpha.common.newId
import com.nitokrisalpha.domain.entity.Magnet
import com.nitokrisalpha.domain.entity.MagnetId
import com.nitokrisalpha.domain.repository.MagnetRepository
import com.nitokrisalpha.domain.service.MagnetProvider
import org.springframework.stereotype.Service

@Service
class MagnetService(
    val magnetRepository: MagnetRepository,
    val magnetProvider: MagnetProvider
) {

    fun recordMagnets(command: MagnetRecordCommand): Collection<MagnetId> {
        val magnets = command.magnetMetaData.map {
            Magnet(newId(), it)
        }
        return magnetRepository.save(magnets = magnets)
    }

    fun searchMagnets(query: String, page: Int) =
        magnetProvider.search(query = query, currentPage = page)

}