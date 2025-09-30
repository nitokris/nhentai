package com.nitokrisalpha.infranstructure.config

import com.nitokrisalpha.common.logger
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
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