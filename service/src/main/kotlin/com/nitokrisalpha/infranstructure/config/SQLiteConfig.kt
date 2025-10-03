package com.nitokrisalpha.infranstructure.config

import com.nitokrisalpha.common.logger
import org.jetbrains.exposed.v1.core.DatabaseConfig
import org.jetbrains.exposed.v1.core.DatabaseConfig.Companion
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.migration.jdbc.MigrationUtils
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.jdbc.SchemaManagement
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ByteArrayResource
import org.springframework.jdbc.datasource.init.DataSourceInitializer
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator
import org.sqlite.JDBC
import javax.sql.DataSource

@Configuration
@ConditionalOnClass(value = [JDBC::class])
class SQLiteConfig {

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
                logger.info("SQLite foreign key support enabled.")
            })
        }
    }
}