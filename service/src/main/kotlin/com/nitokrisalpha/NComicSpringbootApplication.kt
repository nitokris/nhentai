package com.nitokrisalpha

import org.jetbrains.exposed.v1.spring.boot.autoconfigure.ExposedAutoConfiguration
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration

@SpringBootApplication
@ImportAutoConfiguration(
    value = [ExposedAutoConfiguration::class],
    exclude = [DataSourceTransactionManagerAutoConfiguration::class]
)
class NComicSpringbootApplication {
}

fun main(args: Array<String>) {
    SpringApplication.run(NComicSpringbootApplication::class.java, *args)
}