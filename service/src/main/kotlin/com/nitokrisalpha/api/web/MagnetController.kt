package com.nitokrisalpha.api.web

import com.nitokrisalpha.application.command.MagnetRecordCommand
import com.nitokrisalpha.application.service.MagnetService
import com.nitokrisalpha.domain.entity.MagnetId
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("magnet")
class MagnetController(
    private val magnetService: MagnetService
) {
    @PostMapping("")
    fun recordMagnets(@RequestBody command: MagnetRecordCommand): Collection<MagnetId> {
        return magnetService.recordMagnets(command)
    }

    @GetMapping("search")
    fun searchMagnets(@RequestParam("keyword") query: String, page: Int) =
        magnetService.searchMagnets(query, page)
}