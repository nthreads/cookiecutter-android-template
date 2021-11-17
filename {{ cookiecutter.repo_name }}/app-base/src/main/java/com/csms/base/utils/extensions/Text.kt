package com.csms.chaperone.utils.extensions

import android.util.Base64
import android.util.Patterns
import android.view.Gravity
import android.widget.TextView
import com.csms.base.utils.LocaleUtil
import org.json.JSONObject
import java.net.URLDecoder
import java.security.SecureRandom

fun String.ifEmpty(value: String): String {
    if (isEmpty()) {
        return value
    }
    return this
}

fun String.isEmail(): Boolean = Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.encodeToBase64(): String {
    return Base64.encodeToString(this.toByteArray(charset("UTF-8")), Base64.NO_WRAP)
}

fun String.decodeFromBase64(): String {
    return Base64.decode(this, Base64.DEFAULT).toString(charset("UTF-8"))
}

fun String.decodeUrl(): String {
    return URLDecoder.decode(this, "UTF-8")
}

fun String.toJsonObject(): JSONObject {
    return JSONObject(this)
}

fun String?.alignByLocale(vararg views: TextView) {
    if(this != null) {
        if (this.equals("en", true) && LocaleUtil.isRtl()) {
            views.forEach {
                it.gravity = Gravity.END
            }

        } else if (this.equals("ar", true) && !LocaleUtil.isRtl()) {
            views.forEach {
                it.gravity = Gravity.END
            }
        } else if ((this.equals("ar", true) && LocaleUtil.isRtl()) ||
            (this.equals("en", true) && !LocaleUtil.isRtl())) {
            //Do nothing
        } else {
            views.forEach {
                it.gravity = Gravity.CENTER
            }
        }
    } else {
        views.forEach {
            it.gravity = Gravity.CENTER
        }
    }
}

fun generateRandomString(stringLength: Int): String {
    val base = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
    val secureRandom = SecureRandom()

    val stringBuilder = StringBuilder(stringLength)
    for (i in 0 until stringLength) {
        stringBuilder.append(base[secureRandom.nextInt(base.length)])
    }
    return stringBuilder.toString()
}

var TextView.textContent: String
    get() = text.toString().trim()
    set(value) {
        text = value
    }

var TextView.hintContent: String
    get() = hint.toString()
    set(value) {
        hint = value
    }
