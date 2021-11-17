package com.csms.base.utils.extensions

import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import com.csms.chaperone.utils.extensions.hide
import com.csms.chaperone.utils.extensions.show


fun View.rotateBy(value: Float, duration: Long = 100) {
    animate()
        .rotationBy(value)
        .setDuration(duration)
        .start()
}

fun View.fadeIn(startValue: Float = 0f, finishValue: Float = 1f, duration: Long = 200) {
    if (alpha == finishValue) {
        this.show()
        return
    }

    animate()
        .alpha(startValue)
        .setDuration(duration / 2)
        .setInterpolator(DecelerateInterpolator())
        .withEndAction {
            animate()
                .alpha(finishValue)
                .setDuration(duration / 2)
                .setInterpolator(AccelerateInterpolator())
                .start()
        }.start()

    this.show()
}

fun View.fadeOut(startValue: Float = 1f, finishValue: Float = 0f, duration: Long = 200) {
    if (alpha == finishValue) {
        this.hide()
        return
    }

    animate()
        .alpha(startValue)
        .setDuration(duration)
        .setInterpolator(DecelerateInterpolator())
        .withEndAction {
            animate()
                .alpha(finishValue)
                .setDuration(duration)
                .setInterpolator(AccelerateInterpolator())
                .start()
        }.start()

    this.hide()
}

fun View.circularRevealOrUnreveal(
    centerX: Int,
    centerY: Int,
    startRadius: Float,
    endRadius: Float,
    duration: Long = 200
) {
    val anim =
        ViewAnimationUtils.createCircularReveal(this, centerX, centerY, startRadius, endRadius)
    anim.duration = duration

    val isVisible = startRadius < endRadius
    if(isVisible) this.show()
    else this.hide()

    anim.start()
}
