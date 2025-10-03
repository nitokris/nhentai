package com.nitokrisalpha.api.web

import com.nitokrisalpha.common.sha512
import org.springframework.util.StopWatch
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.File

@RestController
@RequestMapping("work/resource")
class WorkFileController {

    @PostMapping("sha512")
    fun testSha512Speed(filePath: String): Map<String, String> {
        val stopwatch = StopWatch()
        stopwatch.start("start")
        val digest = File(filePath).sha512()
        stopwatch.stop()
        val result = HashMap<String, String>()
        result["time"] = stopwatch.prettyPrint()
        result["digest"] = digest
        return result
    }

}