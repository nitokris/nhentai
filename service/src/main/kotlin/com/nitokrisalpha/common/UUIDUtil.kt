package com.nitokrisalpha.common

import java.util.*

fun UUID.noSlashStr() = this.toString().replace("-", "")