package com.example.vinciandroidv2.helper

import android.content.Context
import android.widget.NumberPicker
import androidx.appcompat.app.AlertDialog

fun showNumPicker(
    context: Context,
    list: Array<String>,
    positiveText: String? = null,
    negativeText: String? = null,
    function: (value: Int) -> Unit
) = NumberPicker(context).apply {
    displayedValues = list
    minValue = 0
    maxValue = list.size.minus(1)
}.createAlertDialog(function, positiveText, negativeText)

fun showNumPicker(
    context: Context,
    list: Array<String>,
    setValue: Int,
    positiveText: String? = null,
    negativeText: String? = null,
    function: (value: Int) -> Unit
) = NumberPicker(context).apply {
    displayedValues = list
    minValue = 0
    maxValue = list.size.minus(1)
    if (setValue != -1) value = setValue
}.createAlertDialog(function, positiveText, negativeText)

fun showNumPicker(
    context: Context,
    list: Array<Int>,
    positiveText: String? = null,
    negativeText: String? = null,
    function: (value: Int) -> Unit
) = NumberPicker(context).apply {
    displayedValues = list.map { "$it" }.toTypedArray()
    minValue = list.first()
    maxValue = list.last()
    value = 20
}.createAlertDialog(function, positiveText, negativeText)

fun showNumPicker(
    context: Context,
    list: Array<Int>,
    setValue: Int,
    positiveText: String? = null,
    negativeText: String? = null,
    function: (value: Int) -> Unit
) = NumberPicker(context).apply {
    displayedValues = list.map { "$it" }.toTypedArray()
    minValue = list.first()
    maxValue = list.last()
    if (setValue != -1) value = setValue
}.createAlertDialog(function, positiveText, negativeText)

private fun NumberPicker.createAlertDialog(
    function: (value: Int) -> Unit,
    positiveText: String?,
    negativeText: String?
) {
    AlertDialog.Builder(context).apply {
        setView(this@createAlertDialog)
        setPositiveButton(positiveText ?: "Confirm") { p0, _ ->
            function(this@createAlertDialog.value)
            p0.dismiss()
        }
        setNegativeButton(negativeText ?: "Cancel") { p0, _ -> p0.dismiss() }
        setCancelable(false)
        create()
        show()
    }
}