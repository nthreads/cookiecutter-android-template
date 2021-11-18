package com.nthreads.base.domain.models

data class ErrorResponse(
    val msg: String = "",
    val status: Boolean = false
)