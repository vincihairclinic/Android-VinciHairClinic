package com.example.vinciandroidv2.helper

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.example.vinciandroidv2.R
import kotlinx.android.synthetic.main.view_base_alert.view.*

var isAlertShowing = false

interface AlertListener {
    fun actionNegative() {}
    fun actionPositive() {}
}

//object DialogHelper {
fun showBaseAlert(
    context: Context?,
    icon: String? = null,
    title: String? = null,
    subtitle: String? = null,
    negativeText: String? = null,
    positiveText: String? = null,
    alertListener: AlertListener? = null
) {
    if (isAlertShowing || null == context) return

    val alert = AlertDialog.Builder(context).create()

    val view = LayoutInflater.from(context).inflate(R.layout.view_base_alert, null)?.apply {

        iconField?.apply { text = icon ?: run { isVisible = false; "" } }
        titleField?.apply { text = title ?: run { isVisible = false; "" } }
        subtitleField?.apply { text = subtitle ?: run { isVisible = false; "" } }
        negativeButton?.apply { text = negativeText ?: run { isVisible = false; "" } }
        positiveButton?.apply { text = positiveText ?: run { isVisible = false; "" } }

        negativeButton?.setOnClickListener {
            alert.dismiss()
            isAlertShowing = false
            alertListener?.actionNegative()
        }
        positiveButton?.setOnClickListener {
            alert.dismiss()
            isAlertShowing = false
            alertListener?.actionPositive()
        }
    }

    alert.setView(view)
    alert.setCancelable(false)
    alert.create()
    alert.window?.setBackgroundDrawableResource(android.R.color.transparent)
    alert.show()

    isAlertShowing = true
}
//}