package com.example.vinciandroidv2.ui.global.mvp

import com.example.vinciandroidv2.network.respons.Base
import com.example.vinciandroidv2.ui.global.API_INTERNET_ERROR

interface BaseView {
    fun apiError(
        error: String = API_INTERNET_ERROR,
        apiException: Base? = null,
        function: (() -> Unit)?
    ) {
    }

    fun startLoading() {}
    fun stopLoading() {}
}