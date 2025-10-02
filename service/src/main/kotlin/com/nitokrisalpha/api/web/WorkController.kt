package com.nitokrisalpha.api.web

import com.nitokrisalpha.application.dto.WorkDto
import com.nitokrisalpha.application.service.WorkService
import com.nitokrisalpha.domain.entity.Site
import com.nitokrisalpha.domain.entity.Work
import com.nitokrisalpha.domain.entity.WorkId
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("work")
class WorkController(
    val workService: WorkService
) {

    @GetMapping("recent")
    fun recent(@RequestParam("count") count: Int): Collection<WorkDto> {
        return workService.recent(count)
    }

    @PostMapping("new")
    fun saveNewWork(@RequestParam id: String, site: Site): WorkId {
        return workService.saveNewWork(id, site)
    }


    @GetMapping("{id}")
    fun workDetail(@PathVariable id: String): Work? {
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
}