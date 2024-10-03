package com.example.vinciandroidv2.helper

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import java.util.*

fun showCalendarAndTime(
    context: Context,
    calendar: Calendar = Calendar.getInstance(),
    is24HourView: Boolean = true,
    datePickerMinDateInMillis: Long? = null,
    datePickerMaxDateInMillis: Long? = null,
    function: (calendar: Calendar) -> Unit
) {
    showCalendar(
        context,
        calendar,
        datePickerMinDateInMillis,
        datePickerMaxDateInMillis,
    ) { c ->
        showTime(
            context,
            c,
            is24HourView
        ) { function(it) }
    }
}

fun showCalendar(
    context: Context,
    calendar: Calendar = Calendar.getInstance(),
    datePickerMinDateInMillis: Long? = null,
    datePickerMaxDateInMillis: Long? = null,
    function: (calendar: Calendar) -> Unit,
) {
    DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            function(Calendar.getInstance().apply {
                set(Calendar.DAY_OF_MONTH, dayOfMonth)
                set(Calendar.MONTH, month)
                set(Calendar.YEAR, year)
            })
        },
        calendar[Calendar.YEAR],
        calendar[Calendar.MONTH],
        calendar[Calendar.DAY_OF_MONTH]
    ).apply {
        datePickerMinDateInMillis?.let { min -> datePicker.minDate = min }
        datePickerMaxDateInMillis?.let { max -> datePicker.maxDate = max }
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        show()
    }
}

fun showTime(
    context: Context,
    calendar: Calendar = Calendar.getInstance(),
    is24HourView: Boolean = true,
    function: (calendar: Calendar) -> Unit
) {
    TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            calendar.apply {
                set(Calendar.HOUR_OF_DAY, hourOfDay)
                set(Calendar.MINUTE, minute)
            }
            function(calendar)
        },
        calendar[Calendar.HOUR_OF_DAY],
        calendar[Calendar.MINUTE],
        is24HourView
    ).apply {
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        show()
    }
}