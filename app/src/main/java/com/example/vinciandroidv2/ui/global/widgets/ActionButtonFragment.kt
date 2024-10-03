package com.example.vinciandroidv2.ui.global.widgets

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.model.KeyPath
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.ui.global.dp
import kotlinx.android.synthetic.main.widget_action_button.*

open class ActionButtonFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? = inflater.inflate(R.layout.widget_action_button, container, false)

    private var buttonText: String = ""
    private var isEnabled = true

    fun setText(
        text: String,
        isAllCaps: Boolean = false
    ) {
        with(actionButtonText) {
            this?.text = text
            this?.isAllCaps = isAllCaps
        }
    }

    fun setText(
        text: String,
        textColor: Int,
        font: Int,
        isAllCaps: Boolean = false
    ) {
        with(actionButtonText) {
            this?.text = text
            activity?.getColor(textColor)?.let { this?.setTextColor(it) }
            this?.typeface = ResourcesCompat.getFont(context, font)
            this?.isAllCaps = isAllCaps
        }
    }

    fun setBackgroundColor(color: Int) {
        activity?.getColor(color)?.let { actionButtonCard?.setCardBackgroundColor(it) }
    }

    fun setCardCornersRadius(radius: Int) {
        actionButtonCard?.radius = radius.toFloat()
    }

    fun setBackground(drawable: Int) {
        activity?.getDrawable(drawable)?.let { actionButtonCard?.background = it }
    }

    fun setBorder(
        color: Int,
        strokeWidth: Int,
    ) {
        activity?.getColor(color)?.let { actionButtonCard?.setStrokeColor(it) }
        actionButtonCard?.strokeWidth = strokeWidth.dp
    }

    fun addDrawable(
        @DrawableRes drawable: Int,
        @DimenRes drawablePadding: Int,
        drawablePosition: DrawablePosition,
    ) {
        when (drawablePosition) {
            DrawablePosition.START ->
                actionButtonText?.setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0)
            DrawablePosition.END ->
                actionButtonText?.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable, 0)
        }
        actionButtonText?.compoundDrawablePadding = resources.getDimension(drawablePadding).toInt()
    }

    fun onClick(onClick: (() -> Unit)?) {
        actionButtonCard?.setOnClickListener { onClick?.let { it() } }
    }

    fun isEnabled(isEnabled: Boolean) {
        actionButtonCard?.isEnabled = isEnabled
    }

    fun startLoading() {
        view?.isEnabled = false
        buttonText = actionButtonText?.text?.toString() ?: ""
        actionButtonCard?.isEnabled = false
        actionButtonText?.text = ""
        actionButtonLottie?.playAnimation()
        actionButtonLottie?.visibility = View.VISIBLE
        actionButtonLottie?.addValueCallback(
            KeyPath("**"), LottieProperty.COLOR_FILTER,
            {
                PorterDuffColorFilter(
                    actionButtonText.currentTextColor,
                    PorterDuff.Mode.SRC_ATOP
                )
            })
    }

    fun stopLoading() {
        view?.isEnabled = true
        actionButtonCard?.isEnabled = true
        actionButtonLottie?.cancelAnimation()
        actionButtonLottie?.visibility = View.GONE
        actionButtonText?.text = buttonText
    }

    override fun onDestroyView() {
        actionButtonLottie?.cancelAnimation()
        actionButtonLottie?.visibility = View.GONE
        actionButtonText?.text = buttonText
        super.onDestroyView()
    }
}

enum class DrawablePosition { START, END }
