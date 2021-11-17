package com.csms.chaperone.utils.extensions

import com.csms.base.R
import com.csms.base.domain.models.ErrorResponse
import com.google.gson.GsonBuilder
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException


fun Throwable.getMessageResValue(): Any {

    var errorMsg: Any = R.string.err_went_wrong

    when (this) {
        is HttpException -> {

            val gson = GsonBuilder().create()
            val mError: ErrorResponse
            try {
                val errResponse = response()?.errorBody()?.string() ?: ""
                if (errResponse.isEmpty()) {
                    errorMsg = ""
                } else {
                    mError = gson.fromJson(errResponse, ErrorResponse::class.java)
                    errorMsg = mError.msg
                }
            } catch (e: IOException) {
                errorMsg = when (code()) {
                    400 -> R.string.toast_err_unauthorized
                    401 -> R.string.toast_err_unauthorized
                    403 -> R.string.toast_err_forbidden_req
                    500 -> R.string.toast_err_internal_server
                    else -> R.string.toast_connection_error
                }
            }
        }
        is ConnectException -> {
            errorMsg = R.string.toast_network_error
        }
        is SocketTimeoutException -> {
            errorMsg = R.string.toast_time_out
        }
    }

    return errorMsg
}
