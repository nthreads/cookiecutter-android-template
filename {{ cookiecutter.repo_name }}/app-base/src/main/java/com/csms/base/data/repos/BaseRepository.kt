package com.csms.base.data.repos

import com.csms.base.core.BaseAppController

open class BaseRepository {
    val token : String? = BaseAppController.instance.getSessionAccessToken()
}