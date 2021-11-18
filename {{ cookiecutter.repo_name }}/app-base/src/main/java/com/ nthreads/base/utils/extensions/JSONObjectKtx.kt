package com.nthreads.chaperone.utils.extensions

import org.json.JSONObject

fun JSONObject.getNonNullLong(attrName : String): Long {
    if(!this.isNull(attrName)) {
        return this.getLong(attrName)
    }

    return 0
}