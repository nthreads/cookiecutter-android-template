package com.csms.base.domain.models

data class ErrorResponse(
    val msg: String = "",
    val status: Boolean = false
)