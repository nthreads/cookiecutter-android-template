package com.csms.base.data.responses

import com.google.gson.annotations.SerializedName

class BasicResponse<T>(
    var status: Boolean = false,
    @SerializedName("msg")
    var message: String = "",
    var data: T
)