package com.example.vinciandroidv2.ui.global

import java.text.SimpleDateFormat
import java.util.*

/** date presented as String **/

fun String.changeFormat(outFormat: String, inFormat: String? = null): String? =
    this.parse(inFormat)?.format(outFormat)

fun String.setToTimeInMillis(inFormat: String? = null): Long? =
    this.setToCalendar(inFormat)?.timeInMillis

fun String.setToCalendar(inFormat: String? = null): Calendar? =
    this.setToDate(inFormat)?.let { date -> Calendar.getInstance().apply { time = date } }

fun String.setToDate(inFormat: String? = null): Date? = this.parse(inFormat)


/** date presented as Long **/

fun Long.setToStringFormat(outFormat: String? = null): String = Date(this).format(outFormat)

fun Long.setToCalendar(): Calendar =
    Calendar.getInstance().apply { time = this@setToCalendar.setToDate() }

fun Long.setToDate(): Date = Date(this)


/** date presented as Date **/

fun Date.setToStringFormat(outFormat: String? = null): String = this.format(outFormat)


/** SimpleDateFormat, default format = "yyyy-MM-dd HH:mm:ss" **/

fun Date.format(outFormat: String?): String = outFormat.simpleFormat().format(this)
fun String.parse(inFormat: String?): Date? = inFormat.simpleFormat().parse(this)

fun String?.simpleFormat() = SimpleDateFormat(this ?: "yyyy-MM-dd HH:mm:ss", Locale.getDefault())


/** Seconds to Hours, Minutes, Seconds **/

fun Long.toStringHoursMinutesSeconds() =
    this.toHoursMinutesSeconds().let { String.format("%02d:%02d:%02d", it[0], it[1], it[2]) }

fun Long.toHoursMinutesSeconds() = arrayOf(this / 3600, (this % 3600) / 60, this % 60)
