package com.nitokrisalpha.api.web

import com.nitokrisalpha.application.dto.WorkDto
import com.nitokrisalpha.application.service.WorkQueryService
import com.nitokrisalpha.application.service.WorkService
import com.nitokrisalpha.common.PageResponse
import com.nitokrisalpha.domain.entity.FileEntity
import com.nitokrisalpha.domain.entity.Site
import com.nitokrisalpha.domain.entity.Work
import com.nitokrisalpha.domain.entity.WorkId
import com.nitokrisalpha.infranstructure.jdbc.table.Works.site
import org.http4k.lens.StringBiDiMappings.url
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.function.RequestPredicates.param

@RestController
@RequestMapping("work")
class WorkController(
    val workService: WorkService,
    val workQueryService: WorkQueryService
) {

    @GetMapping("recent")
    fun recent(@RequestParam("count") count: Int): Collection<WorkDto> {
        return workService.recent(count)
    }

    @GetMapping
    fun page(@RequestParam page: Int, @RequestParam pageSize: Int): PageResponse<WorkDto> {
        return workQueryService.page(page, pageSize)
    }

    @PostMapping("new")
    fun saveNewWork(@RequestParam id: String, site: Site): WorkId {
        return workService.saveNewWork(id, site)
    }

    @PostMapping("url")
    fun saveWorkUrl(@RequestBody param: Map<String, String>): Unit {
        return workService.saveNewWork(param["url"] as String)
    }


    @GetMapping("{id}")
    fun workDetail(@PathVariable id: String): WorkDto? {
        val result = workService.findOne(id)
        return result
    }

    @DeleteMapping("{id}")
    fun dropWork(@PathVariable id: String) {
        workService.dropWork(id)
    }

    @PutMapping("{id}/magnets")
    fun addMagnets(@PathVariable id: String, @RequestBody magnets: Collection<String>) {
        workService.addMagnets(id, magnets)
    }

    @DeleteMapping("{id}/magnets")
    fun removeMagnets(@PathVariable id: String, @RequestBody magnets: Collection<String>) {
        workService.removeMagnets(id, magnets)
    }

    @PostMapping("{id}/resource")
    fun bindFileToWork(@PathVariable id: String, @RequestBody filePath: Map<String, String>) {
        workService.bindFile(id, filePath["filePath"] as String)
    }

    @GetMapping("resource/{resourceId}")
    fun readWorkFile(
        @PathVariable resourceId: String
    ): Collection<FileEntity> {
        return workQueryService.readWork(resourceId)
    }

}