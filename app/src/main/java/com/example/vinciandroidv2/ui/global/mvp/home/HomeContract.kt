package com.example.vinciandroidv2.ui.global.mvp.home

import com.example.vinciandroidv2.network.respons.BaseHome
import com.example.vinciandroidv2.ui.global.mvp.BaseView

interface HomeContract {
    interface View : BaseView {
        fun homeLoaded(baseHome: BaseHome) {}
    }

    interface Presenter {
        fun getHome()
    }
}