package com.nitokrisalpha

import com.nitokrisalpha.infranstructure.config.Http4kConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class NcomicSpringbootApplication

fun main(args: Array<String>) {
    runApplication<NcomicSpringbootApplication>(*args)
}
