package com.example.vinciandroidv2.helper

import android.widget.TextView

fun TextView.setStartDrawable(resId: Int) {
    setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0)
}
fun TextView.setStartEndDrawable(resId: Int, resIdSecond: Int) {
    setCompoundDrawablesWithIntrinsicBounds(resId, 0, resIdSecond, 0)
}

fun TextView.setTopDrawable(resId: Int) {
    setCompoundDrawablesWithIntrinsicBounds(0, resId, 0, 0)
}

fun TextView.setEndDrawable(resId: Int) {
    setCompoundDrawablesWithIntrinsicBounds(0, 0, resId, 0)
}

fun TextView.setBottomDrawable(resId: Int) {
    setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, resId)
}