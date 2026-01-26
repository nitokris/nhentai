package com.nitokrisalpha.config

import org.jetbrains.exposed.v1.core.DatabaseConfig
import org.springframework.boot.SpringBootConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.core.io.ByteArrayResource
import org.springframework.jdbc.datasource.init.DataSourceInitializer
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator
import javax.sql.DataSource

@SpringBootConfiguration
class ExposedConfig {
    @Bean
    open fun databaseConfig(): DatabaseConfig {
        return DatabaseConfig {
            useNestedTransactions = true
        }
    }

    @Bean
    fun dataSourceInitializer(dataSource: DataSource): DataSourceInitializer {
        return DataSourceInitializer().apply {
            setDataSource(dataSource)
            setDatabasePopulator(ResourceDatabasePopulator().apply {
                addScript(ByteArrayResource("PRAGMA foreign_keys = ON;".toByteArray()))
            })
        }
    }
}