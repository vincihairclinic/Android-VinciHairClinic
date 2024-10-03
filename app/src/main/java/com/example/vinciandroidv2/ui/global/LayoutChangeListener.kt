package com.example.vinciandroidv2.ui.global

import android.os.Handler
import android.os.Looper
import android.view.View

class LayoutChangeListener : View.OnLayoutChangeListener {

    interface Delegate {
        fun layoutDidChange(oldHeight: Int, newHeight: Int, tempBottom: Int)
    }

    private val uiHandler = Handler(Looper.getMainLooper())

    var delegate: Delegate? = null

    override fun onLayoutChange(v: View, left: Int, top: Int, right: Int, bottom: Int,
                                oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
        val currentHeight = bottom - top
        val previousHeight = oldBottom - oldTop

        var tempBottom = bottom - oldBottom
        if (tempBottom < 0) {
            tempBottom = oldBottom - bottom
        }
        if (currentHeight == previousHeight || tempBottom < 200) return

        uiHandler.post { delegate?.layoutDidChange(previousHeight, currentHeight, tempBottom) }
    }
}