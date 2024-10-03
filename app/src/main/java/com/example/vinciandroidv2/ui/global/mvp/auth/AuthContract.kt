package com.example.vinciandroidv2.ui.global.mvp.auth

import com.example.vinciandroidv2.network.respons.User
import com.example.vinciandroidv2.ui.global.mvp.BaseView
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthContract {
    interface View : BaseView {
        fun userLoggedAuth() {}
        fun passwordForgotRequestSent() {}
        fun passwordChanged() {}
    }

    interface Presenter {
        fun login(email: String, password: String)
        fun register(user: User)
        fun forgotPassword(email: String)
        fun changePassword(currentPassword: String, newPassword: String)

        fun autoRegister(
            countryId: Int,
            languageKey: String,
            appState: String
        )
    }
}