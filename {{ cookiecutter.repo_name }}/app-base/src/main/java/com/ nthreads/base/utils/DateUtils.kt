package com.nthreads.base.utils

import android.annotation.SuppressLint
import android.text.TextUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    const val PATTERN_1 = "dd/MM/yyyy"
    const val PATTERN_2 = "MMM d ''yy"
    const val PATTERN_3 = "EEE, MMM d, ''yy"
    const val PATTERN_4 = "EEE, MMM d, hh:mm a"
    const val PATTERN_5 = "hh:mm a - MMM d, yyyy"
    const val PATTERN_6 = "hh:mm a"
    const val PATTERN_7 = "hh:mm a"
    const val PATTERN_8 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" //2019-06-18T13:48:19.061Z
    const val PATTERN_9 = "MMM d, yyyy" // Jul 4, '1990
    const val PATTERN_10 = "yyyy-MM-dd" // 2021-07-07
    const val PATTERN_11 = "dd MMM" // 09 July

    @SuppressLint("SimpleDateFormat")
    fun timestampToFormattedString(dob: Long): String {
        val date = Date(dob * 1000L)
        val sdf = SimpleDateFormat(PATTERN_1)
        return sdf.format(date)
    }

    fun getTimestamp(date: String, pattern: String = PATTERN_8): Long {
        val sdf = SimpleDateFormat(pattern, Locale.ENGLISH)
        try {
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            val parsedDate = sdf.parse(date)
            return parsedDate?.time ?: 0
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return 0
    }

    fun getTimestampInSeconds(date: String): Long {
        val sdf = SimpleDateFormat(PATTERN_8, Locale.ENGLISH)
        try {
            val parsedDate = sdf.parse(date)
            return parsedDate.time / 1000L //Unix time stamp in seconds
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return 0
    }

    fun toDate(date: String): Date {
        if (TextUtils.isEmpty(date)) return Date()

        val sdf = SimpleDateFormat(PATTERN_1, Locale.ENGLISH)
        try {
            return sdf.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return Date()
    }

    fun format(date: Date, format: String, locale: Locale = Locale.getDefault()): String {
        val sdf = SimpleDateFormat(format, locale)
        return sdf.format(date)
    }


    fun format(timestampInMillis: Long, format: String, locale: Locale = Locale.getDefault()): String {
        val sdf = SimpleDateFormat(format, locale)
        return sdf.format(Date(timestampInMillis))
    }

    fun format(
            dateStr: String,
            actualFormat: String,
            newFormat: String,
            localeEnglish: Boolean = false
    ): String {

        val sdf = SimpleDateFormat(actualFormat, Locale.ENGLISH)
        try {
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            val parsedDate: Date = sdf.parse(dateStr) ?: return ""
            val newSdf = SimpleDateFormat(
                    newFormat,
                    if (localeEnglish) Locale.ENGLISH else Locale.getDefault()
            )
            //newSdf.timeZone = TimeZone.getTimeZone("GMT+4")
            return newSdf.format(parsedDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }

        return "--"
    }

    fun getDaysInRange(startDate: Long, endDate: Long): Long {
        val diff = endDate - startDate
        return diff / (1000 * 60 * 60 * 24)
    }

    fun getDaysInRange(startDate: Date?, endDate: Date?): Long {
        var days = 1
        val cal = Calendar.getInstance()
        cal.time = startDate
        while (cal.time.before(endDate)) {
            days += 1
            cal.add(Calendar.DATE, 1)
        }
        return days.toLong()
    }

    fun getUnixTimeInMilliSeconds(year: Int, month: Int, day: Int, hour: Int, minute: Int): Long {
        val cal = Calendar.getInstance()
        cal[Calendar.YEAR] = year
        cal[Calendar.MONTH] = month
        cal[Calendar.DAY_OF_MONTH] = day
        cal[Calendar.HOUR_OF_DAY] = hour
        cal[Calendar.MINUTE] = minute
        cal[Calendar.SECOND] = 0
        return cal.timeInMillis
    }

    fun getUnixTimeInSeconds(year: Int, month: Int, day: Int, hour: Int, minute: Int): Long {
        val cal = Calendar.getInstance()
        cal[Calendar.YEAR] = year
        cal[Calendar.MONTH] = month
        cal[Calendar.DAY_OF_MONTH] = day
        cal[Calendar.HOUR_OF_DAY] = hour
        cal[Calendar.MINUTE] = minute
        cal[Calendar.SECOND] = 0
        return cal.timeInMillis / 1000L
    }

    @SuppressLint("DefaultLocale")
    fun getFormattedTime(timeStamp: Long): String {
        val cal = Calendar.getInstance()
        cal.timeInMillis = timeStamp * 1000L
        return String.format("%02d:%02d", cal[Calendar.HOUR_OF_DAY], cal[Calendar.MINUTE])
    }

    @JvmStatic
    fun toDateString(): String {
        var dateString = ""
        val formatter = SimpleDateFormat("EEE MMM dd HH:mm yyyy", Locale.getDefault()) //Sun Apr 09 13:40:47 +0000 2017
        dateString = formatter.format(Date())
        return dateString
    }

    @JvmStatic
    fun toDate(date: Date): String {
        val formatter = SimpleDateFormat("EEE MMM dd HH:mm yyyy", Locale.getDefault()) //Sun Apr 09 13:40:47 +0000 2017
        return formatter.format(date)
    }

    fun toDate(createdAt: Long): String {
        return toDate(Date(createdAt))
    }

    @JvmStatic
    fun toDate(dateString: String, locale: Locale): Date {
        var date = Date()
        val formatter = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", locale) //Sun Apr 09 13:40:47 +0000 2017
        try {
            date = formatter.parse(dateString) ?: Date()
        } catch (e1: ParseException) {
            e1.printStackTrace()
        }
        return date
    }

    fun toDate(dateString: String, format: String): Date {
        var date = Date()
        val formatter = SimpleDateFormat(format, Locale.getDefault()) //Sun Apr 09 13:40:47 +0000 2017
        try {
            date = formatter.parse(dateString) ?: Date()
        } catch (e1: ParseException) {
            e1.printStackTrace()
        }
        return date
    }

    fun toSeconds(seconds: Double): String {
        val minutes = seconds / 60
        return String.format("%.2f", minutes)
    }
}