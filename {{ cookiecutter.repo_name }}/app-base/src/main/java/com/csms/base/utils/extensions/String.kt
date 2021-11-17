package com.csms.chaperone.utils.extensions

import android.graphics.Color
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.util.Patterns
import org.json.JSONObject

fun String.removeTrailingSlash(): String {
    var removed = this
    while (removed.isNotEmpty() && removed[removed.length - 1] == '/') {
        removed = removed.substring(0, removed.length - 1)
    }
    return removed
}

fun String.sanitize(): String {
    val tmp = this.trim()
    return tmp.removeTrailingSlash()
}

fun String.removeLeadingZeros(): String {
    var removed = this.sanitize()
    while (removed.isNotEmpty() && removed[0] == '0') {
        removed = removed.substring(1, removed.length)
    }

    return removed
}


fun String.avatarUrl(
    avatar: String,
    isGroupOrChannel: Boolean = false,
    format: String = "jpeg"
): String {
    return if (isGroupOrChannel) {
        "${removeTrailingSlash()}/avatar/%23${avatar.removeTrailingSlash()}?format=$format"
    } else {
        "${removeTrailingSlash()}/avatar/${avatar.removeTrailingSlash()}?format=$format"
    }
}

fun String?.mask(): String {
    return if (this.isNotNullNorEmpty()) {
        this?.replace("-","")?.apply {
            return if (length < 15)
                this
            else
                String.format(
                    "%s-%s-%s-%s",
                    substring(0, 3),
                    substring(3, 7),
                    substring(7, length - 1),
                    substring(length - 1, length)
                )
        } ?: "--"

    } else "--"

}

fun String.isImage(): Boolean {
    return endsWith(".gif") ||endsWith(".GIF") || endsWith(".png")|| endsWith(".PNG") || endsWith(".jpg") || endsWith(".JPG") || endsWith("jpeg") || endsWith("JPEG") || endsWith("jfif") || endsWith("JFIF")
}


fun String.isAudio(): Boolean {
    return endsWith(".m4a") || endsWith(".mp3") || endsWith(".3gp") || endsWith(".aac") || endsWith(".wav") || endsWith(".aac")
}

fun String.isVideo(): Boolean {
    return endsWith(".mp4") || endsWith(".mov") || endsWith(".flv") || endsWith(".MP4") || endsWith(".MOV") || endsWith(".FLV")
}

fun String.safeUrl(): String {
    return this.replace(" ", "%20").replace("\\", "")
}

fun String.serverLogoUrl(favicon: String) = "${removeTrailingSlash()}/$favicon"

fun String.casUrl(serverUrl: String, casToken: String) =
    "${removeTrailingSlash()}?service=${serverUrl.removeTrailingSlash()}/_cas/$casToken"

fun String.samlUrl(provider: String, samlToken: String) =
    "${removeTrailingSlash()}/_saml/authorize/$provider/$samlToken"

fun String.termsOfServiceUrl() = "${removeTrailingSlash()}/terms-of-service"

fun String.privacyPolicyUrl() = "${removeTrailingSlash()}/privacy-policy"

fun String.adminPanelUrl() = "${removeTrailingSlash()}/admin/info?layout=embedded"

fun String.isValidUrl(): Boolean = Patterns.WEB_URL.matcher(this).matches()

fun String.parseColor(): Int {
    return try {
        Color.parseColor(this)
    } catch (exception: IllegalArgumentException) {
        // Log the exception and get the white color.
        exception.message?.let { Log.e("String", it) }
        Color.parseColor("white")
    }
}

fun String.userId(userId: String?): String? {
    return userId?.let { this.replace(it, "") }
}

fun String?.isNotNullNorEmpty(): Boolean = this != null && this.isNotEmpty()

inline fun String?.ifNotNullNotEmpty(block: (String) -> Unit) {
    if (this != null && this.isNotEmpty()) {
        block(this)
    }
}


fun String?.parseHtml(): Spanned {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        return Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        @Suppress("deprecation")
        return Html.fromHtml(this)
    }
}

fun String?.getDataInSuccess(): JSONObject? {
    return if (this.isNotNullNorEmpty()) {
        val responseJObj = JSONObject(this!!)
        if (responseJObj.getBoolean("status")) responseJObj.getJSONObject("data") else null
    } else null
}

fun String?.getMessageInError(): String? {
    return if (this.isNotNullNorEmpty()) {
        val responseJObj = JSONObject(this!!)
        responseJObj.getString("message")
    } else null
}

fun Any?.ifNull(block: () -> Unit) {
    if (this == null) block()
}