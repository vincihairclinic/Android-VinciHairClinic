package com.example.vinciandroidv2.di.module

import com.example.vinciandroidv2.ui.global.mvp.home.HomeContract
import com.example.vinciandroidv2.ui.global.mvp.home.HomePresenterImpl
import toothpick.config.Module

class HomeModule(homeView: HomeContract.View) : Module() {
    init {
        bind(HomeContract.View::class.java).toInstance(homeView)
        bind(HomeContract.Presenter::class.java).to(HomePresenterImpl::class.java).singleton()
    }
}