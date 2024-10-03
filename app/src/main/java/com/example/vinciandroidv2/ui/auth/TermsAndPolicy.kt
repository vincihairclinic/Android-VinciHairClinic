package com.example.vinciandroidv2.ui.auth

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.ui.global.getTypeFace

fun TextView?.showTermsAndPolicyText(
    context: Context?,
    fullText: String,
    spanOneText: String,
    spanOneLink: String,
    spanTwoText: String,
    spanTwoLink: String
) {
    if (null == context) return

    val spanOne = object : ClickableSpan() {
        override fun onClick(widget: View) {
            widget.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(spanOneLink)))
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.color = context.resources.getColor(R.color.primary_brown_7D5F2B)
            ds.isUnderlineText = true
            ds.typeface = getTypeFace(context, R.font.montserrat_bold)
        }
    }

    val spanTwo = object : ClickableSpan() {
        override fun onClick(widget: View) {
            widget.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(spanTwoLink)))
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.color = context.resources.getColor(R.color.primary_brown_7D5F2B)
            ds.isUnderlineText = true
            ds.typeface = getTypeFace(context, R.font.montserrat_bold)
        }
    }

    val spannableString = SpannableString(fullText)

    try {
        val spanOneIndex = spannableString.indexOf(spanOneText)
        val spanTwoIndex = spannableString.indexOf(spanTwoText)

        spannableString.setSpan(
            spanOne,
            spanOneIndex,
            spanOneIndex + spanOneText.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            spanTwo,
            spanTwoIndex,
            spanTwoIndex + spanTwoText.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    } catch (e: Exception) {
        e.printStackTrace()
    }
    this?.linksClickable = true
    this?.movementMethod = LinkMovementMethod.getInstance()
    this?.setBackgroundResource(0)
    this?.highlightColor = context.resources.getColor(R.color.primary_brown_7D5F2B)

    this?.text = spannableString
}