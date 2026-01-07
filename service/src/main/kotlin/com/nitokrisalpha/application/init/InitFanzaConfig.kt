package com.nitokrisalpha.application.init

import com.nitokrisalpha.application.adapter.db.exposed.FanzaConfigDB
import com.nitokrisalpha.application.configuration.FANZAApiProperties
import com.nitokrisalpha.application.logging.log
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner

//@Component
//@Order(Ordered.HIGHEST_PRECEDENCE)
class InitFanzaConfig(
    val fanzaApiProperties: FANZAApiProperties,

    ) : ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        log.info("start init fanza config")
        transaction {
            val rs = FanzaConfigDB.selectAll()
                .where { FanzaConfigDB.type eq "FANZA" }
                .firstOrNull()
            rs?.let {
                fanzaApiProperties.cookie = it[FanzaConfigDB.cookie]
            }
        }
        log.info("end init fanza config")
    }
}