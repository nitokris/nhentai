package com.nitokrisalpha.api.web

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("handle")
class HandleController {

    @PostMapping("tasks")
    fun batchAddTask(urls: List<String>) {

    }
}