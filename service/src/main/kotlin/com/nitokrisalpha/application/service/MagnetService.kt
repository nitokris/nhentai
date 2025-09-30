package com.nitokrisalpha.application.service

import com.nitokrisalpha.application.command.MagnetRecordCommand
import com.nitokrisalpha.common.newId
import com.nitokrisalpha.domain.entity.Magnet
import com.nitokrisalpha.domain.entity.MagnetId
import com.nitokrisalpha.domain.repository.MagnetRepository
import org.springframework.stereotype.Service

@Service
class MagnetService(
    val magnetRepository: MagnetRepository
) {

    fun recordMagnets(command: MagnetRecordCommand): Collection<MagnetId> {
        val magnets = command.magnetMetaData.map {
            Magnet(newId(), it)
        }
        return magnetRepository.save(magnets = magnets)
    }

}