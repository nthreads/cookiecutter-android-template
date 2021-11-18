package com.nthreads.base.domain.models

import com.nthreads.base.R
import com.nthreads.base.domain.models.Resource.Status.*

//This class describes data with a status
class Resource<T> constructor(
    val status: Status,
    val data: T? = null,
    val message: String? = null,
    val messageRes: Int? = null
) {
    enum class Status {
        SUCCESS, ERROR, LOADING, EMPTY
    }

    companion object {

        fun <T> success(data: T, msg: String? = ""): Resource<T> {
            return Resource(SUCCESS, data = data, message = msg)
        }

        fun <T> empty(): Resource<T> {
            return Resource(status = EMPTY)
        }

        fun <T> error(msg: String?): Resource<T> {
            return Resource(status = ERROR, message = msg)
        }

        fun <T> error(msg: Int?): Resource<T> {
            return Resource(status = ERROR, messageRes = msg)
        }

        fun <T> error(msg: Any): Resource<T> {

            return when (msg) {
                is Int -> Resource(status = ERROR, messageRes = msg)
                is String -> Resource(status = ERROR, message = msg)
                else -> Resource(status = ERROR, messageRes = R.string.err_went_wrong)
            }
        }

        fun <T> loading(): Resource<T> {
            return Resource(status = LOADING)
        }
    }
}