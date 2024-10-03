package com.example.vinciandroidv2.helper

import android.graphics.LinearGradient
import android.graphics.Shader
import android.text.TextPaint
import android.text.style.CharacterStyle
import android.text.style.UpdateAppearance
import androidx.annotation.ColorInt

class LinearGradientSpan(
    @ColorInt private val startColorInt: Int,
    @ColorInt private val endColorInt: Int
) : CharacterStyle(), UpdateAppearance {
    override fun updateDrawState(tp: TextPaint?) {
        tp ?: return
        tp.shader = LinearGradient(
            0f,
            0f,
            0f,
            90f,
            startColorInt,
            endColorInt,
            Shader.TileMode.REPEAT
        )
    }
}