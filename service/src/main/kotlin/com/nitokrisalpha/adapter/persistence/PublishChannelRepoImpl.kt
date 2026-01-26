package com.nitokrisalpha.adapter.persistence

import com.nitokrisalpha.adapter.persistence.exposed.PublishChannelTable
import com.nitokrisalpha.business.entity.PublishChannel
import com.nitokrisalpha.business.repository.PublishChannelRepo
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class PublishChannelRepoImpl : PublishChannelRepo {
    override fun findOne(channelId: Long): PublishChannel? {
        TODO("Not yet implemented")
    }

    override fun save(channels: List<PublishChannel>) {

        transaction {
            for (channel in channels) {
                PublishChannelTable.insert {
                    it[name] = channel.name
                    it[accessUrl] = channel.accessUrl
                }
            }
            commit()
        }
    }
}