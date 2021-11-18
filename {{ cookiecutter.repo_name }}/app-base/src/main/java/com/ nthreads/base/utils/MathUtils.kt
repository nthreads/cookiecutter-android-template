package com.nthreads.base.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import java.text.DecimalFormat
import java.util.*

object MathUtils {
    @JvmStatic
    fun getPercentage(ofValue: Float, fromTotal: Float): Float {
        return ofValue * 100.0f / fromTotal
    }


    fun getColumnsByPercent(ofValue: Float, offset: Float = 0F): Float {
        val screenWidthPx = Resources.getSystem().displayMetrics.widthPixels - dpToPx(offset)

        return (screenWidthPx * ofValue) / 100
    }

    fun getColumnWidth(context: Context, noOfColumns: Int): Int? {
        val displayMetrics = context.resources?.displayMetrics
        val screenWidthDp = displayMetrics?.density?.let { displayMetrics.widthPixels / it }
        val columnWidth: Int? = screenWidthDp?.let { (it / noOfColumns).toInt() }

        return columnWidth
    }

    /**
     * Converts DIP to PX.
     * @param dp initial value
     * @return converted value
     */
    fun dpToPx(dp: Float): Float {
        return dp * Resources.getSystem().displayMetrics.density
    }

    /**
     * Converts PX to DIP.
     * @param px initial value
     * @return converted value
     */
    fun pxToDp(px: Float): Float {
        return px / Resources.getSystem().displayMetrics.density
    }

    /**
     * Converts PX to MM (millimeters).
     *
     * @param px      initial value
     * @param context context
     * @return converted value
     */
    fun pxToMm(px: Float, context: Context): Float {
        val dm = context.resources.displayMetrics
        return px / TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, 1f, dm)
    }

    fun withSuffix(count: Long): String {
        if (count < 1000) return "" + count
        val exp = (Math.log(count.toDouble()) / Math.log(1000.0)).toInt()
        return String.format(Locale.ENGLISH, "%.1f%c", count / Math.pow(1000.0, exp.toDouble()), "kMGTPE"[exp - 1])
    }

    @JvmStatic
    fun readableFileSize(size: Long): String {
        if (size <= 0) return "0 B"
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (Math.log10(size.toDouble()) / Math.log10(1024.0)).toInt()
        return DecimalFormat("#,##0.#").format(size / Math.pow(1024.0, digitGroups.toDouble())) + " " + units[digitGroups]
    }

    @JvmStatic
    @SuppressLint("DefaultLocale")
    fun getFormattedCountWithPercentage(count: Long, totalOf: Long): String {
        return String.format("%s - %.2f %s", withSuffix(count), getPercentage(count.toFloat(), totalOf.toFloat()), "%")
    }

    @JvmStatic
    fun getPercentageWithSuffix(count: Long, totalOf: Long): String {
        return String.format("%.2f%s", getPercentage(count.toFloat(), totalOf.toFloat()), "%")
    }

}