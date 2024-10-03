package com.example.vinciandroidv2.helper

import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.setDivider(@DrawableRes drawableRes: Int) {

    val drawable = ContextCompat.getDrawable(this.context, drawableRes) ?: return
    val divider = DividerItemDecoration(
        this.context,
        DividerItemDecoration.VERTICAL
    )

    divider.setDrawable(drawable)
    addItemDecoration(divider)
}