package com.nitokrisalpha.domain.entity

enum class Site {

    DLSITE,
    FANZA,
    MELON,
    UNKNOWN;

    companion object {
        fun fromUrl(url: String): Site {
            if(url.contains("dmm.co.jp")){
                return FANZA
            }
            return UNKNOWN
        }
    }
}