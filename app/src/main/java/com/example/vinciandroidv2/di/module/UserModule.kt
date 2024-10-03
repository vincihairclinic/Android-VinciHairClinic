package com.example.vinciandroidv2.di.module

import toothpick.config.Module
import com.example.vinciandroidv2.ui.global.mvp.user.UserContract
import com.example.vinciandroidv2.ui.global.mvp.user.UserPresenterImpl

class UserModule(userView: UserContract.View) : Module() {
    init {
        bind(UserContract.View::class.java).toInstance(userView)
        bind(UserContract.Presenter::class.java).to(UserPresenterImpl::class.java).singleton()
    }
}