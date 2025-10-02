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
            if(url.contains("melonbooks.co.jp")){
                return MELON
            }
            return UNKNOWN
        }
    }
}