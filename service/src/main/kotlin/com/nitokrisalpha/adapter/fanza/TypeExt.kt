package com.nitokrisalpha.adapter.fanza

import com.nitokrisalpha.business.entity.Type

fun String.Companion.toType(value: String): Type {
    return when (value) {
        "コミック" -> Type.COMIC
        "CG" -> Type.CG
        "ゲーム" -> Type.GAME
        else -> Type.OTHER
    }
}