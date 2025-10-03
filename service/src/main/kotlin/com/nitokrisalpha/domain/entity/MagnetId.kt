package com.nitokrisalpha.domain.entity

import com.fasterxml.jackson.annotation.JsonProperty

class MagnetId(
    @get:JsonProperty("id")
    override val value: String
) : Id {
}