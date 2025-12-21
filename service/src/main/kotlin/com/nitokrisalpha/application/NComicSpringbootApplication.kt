package com.nitokrisalpha.application

import com.nitokrisalpha.application.configuration.FANZAApiProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(FANZAApiProperties::class)
class NComicSpringbootApplication

fun main(args: Array<String>) {
    runApplication<NComicSpringbootApplication>(*args)
}