package com.nitokrisalpha

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
@ConfigurationPropertiesScan
class NComicSpringbootApplication

fun main(args: Array<String>) {
    runApplication<NComicSpringbootApplication>(*args)
}
