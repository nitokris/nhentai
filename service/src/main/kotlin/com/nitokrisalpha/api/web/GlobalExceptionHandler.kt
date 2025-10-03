package com.nitokrisalpha.api.web

import org.apache.hc.core5.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {


    @ExceptionHandler(exception = [Exception::class])
    @ResponseStatus()
    fun handleException(e: Exception): String {
        return "error:${e.message}"
    }

}