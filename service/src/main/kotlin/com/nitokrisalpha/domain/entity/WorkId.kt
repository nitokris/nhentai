package com.nitokrisalpha.domain.entity

import com.fasterxml.jackson.annotation.JsonProperty

class WorkId(
    @get:JsonProperty("id")
    override val value: String
) : Id {

}