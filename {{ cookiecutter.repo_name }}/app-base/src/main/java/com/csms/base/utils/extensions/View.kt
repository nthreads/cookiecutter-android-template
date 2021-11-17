package com.csms.chaperone.utils.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.animation.LinearInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import com.csms.base.utils.SingleClickListener

fun View.hide() {
    this.visibility = View.INVISIBLE
}

fun View.disable() {
    this.isEnabled = false
}

fun View.enable() {
    this.isEnabled = true
}

fun LinearLayout.enableWithChildren() {
    this.isEnabled = true

    for (i in 0 until this.childCount) {
        val view: View = this.getChildAt(i)
        view.isEnabled = true // Or whatever you want to do with the view.
    }
}

fun LinearLayout.disableWithChildren() {
    this.isEnabled = false

    for (i in 0 until this.childCount) {
        val view: View = this.getChildAt(i)
        view.isEnabled = false // Or whatever you want to do with the view.
    }
}

fun View.gone() {
    val z = this.translationZ
    this.visibility = View.GONE
    this.animate().scaleX(0f).scaleY(0f).setDuration(100).setInterpolator(LinearInterpolator())
        .start()
    this.translationZ = z
}

fun View.show() {
    val z = this.translationZ
    this.visibility = View.VISIBLE
    this.animate().scaleX(1f).scaleY(1f).setDuration(100).setInterpolator(LinearInterpolator())
        .start()
    this.translationZ = z
}

fun View.isVisible(): Boolean {
    return this.visibility == View.VISIBLE
}

fun View.toggleVisibility() {
    if (this.isVisible()) this.hide()
    else this.show()
}

fun View.setViewVisibility(show: Boolean) {
    if (show) this.show()
    else this.hide()
}

fun ImageView.load(url: String?) {
    /* Glide.with(context)
             .load(url)
             .crossFade()
             .placeholder(R.drawable.ic_placeholder)
             .error(R.drawable.ic_placeholder)
             .into(this)*/

    /*Glide.with(this)
            .load(url)
            .apply(RequestOptions()
                    .placeholder(R.drawable.ic_placeholder))
            .into(this)*/
}

fun getColoredString(text: CharSequence, color: Int): Spannable? {
    if (TextUtils.isEmpty(text)) {
        return null
    }

    val spannableString = SpannableString(text)
    spannableString.setSpan(
        ForegroundColorSpan(color),
        0,
        text.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    return spannableString
}

fun hideKeyboard(activity: Activity) {
    val view = activity.currentFocus

    view?.let {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun View.setSingleOnClickListener(onSingleClick: (View) -> Unit) {
    val singleClickListener = SingleClickListener {
        onSingleClick(it)
    }
    setOnClickListener(singleClickListener)
}

fun View.dialNumber(context: Context, contactNumber: String) {
    val intent = Intent(Intent.ACTION_DIAL)
    intent.data = Uri.parse("tel:$contactNumber")
    context.startActivity(intent)
}
