package com.nthreads.base.data.repos

import com.nthreads.base.core.BaseAppController

open class BaseRepository {
    val token : String? = BaseAppController.instance.getSessionAccessToken()
}