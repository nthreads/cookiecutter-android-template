package com.csms.base.core.enums

/**
 * Package Name : com.csms.base.core.enums
 * Created by Muhammad Nauman Zubair on 2019-09-12 - 22:18.
 * Copyright (c) 2017 Creativity Smart Media Solutions. All rights reserved.
 */
enum class HttpCode (val code: Int) {
    SUCCESS(200),
    UNAUTHORIZED(401),
    LOGOUT(-1)
}
