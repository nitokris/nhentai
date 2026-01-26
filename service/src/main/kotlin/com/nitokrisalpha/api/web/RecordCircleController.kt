package com.nitokrisalpha.api.web

import com.nitokrisalpha.service.RecordCircleService
import com.nitokrisalpha.values.param.RecordCircleParam
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("circle/record")
class RecordCircleController(
    private val recordCircleService: RecordCircleService
) {

    @PostMapping()
    fun recordCircle(param: RecordCircleParam) {
        recordCircleService.recordCircle(param)
    }

}