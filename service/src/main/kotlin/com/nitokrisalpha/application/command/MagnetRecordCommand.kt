package com.nitokrisalpha.application.command

import com.nitokrisalpha.domain.entity.MagnetMetaData

data class MagnetRecordCommand(
    val magnetMetaData: Collection<MagnetMetaData>
)