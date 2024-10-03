package com.example.vinciandroidv2.di.module

import toothpick.config.Module
import com.example.vinciandroidv2.ui.global.mvp.auth.AuthContract
import com.example.vinciandroidv2.ui.global.mvp.auth.AuthPresenterImpl

class AuthModule(authView: AuthContract.View) : Module() {
    init {
        bind(AuthContract.View::class.java).toInstance(authView)
        bind(AuthContract.Presenter::class.java).to(AuthPresenterImpl::class.java).singleton()
    }
}