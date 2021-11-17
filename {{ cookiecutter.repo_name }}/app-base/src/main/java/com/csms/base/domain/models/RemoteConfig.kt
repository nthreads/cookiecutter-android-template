package com.csms.base.domain.models

import com.google.gson.annotations.SerializedName

/**
 * Package Name : com.csms.base.domain.models
 * Created by Muhammad Nauman Zubair on 3/15/20 - 12:34 PM.
 * Copyright (c) 2017 Creativity Smart Media Solutions. All rights reserved.
 */
data class RemoteConfig(

    @SerializedName("android_config")
    var appConfig: AppConfig = AppConfig()

) {

    fun isAppUpdateNeeded(installedVersion: Int): Boolean =
        appConfig.appVersion > installedVersion
}

data class AppConfig(
    @SerializedName("app_version")
    var appVersion: Int = 1,
)

enum class Status {
    up, down
}