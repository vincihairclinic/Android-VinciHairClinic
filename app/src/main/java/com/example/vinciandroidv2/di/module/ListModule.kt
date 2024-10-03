package com.example.vinciandroidv2.di.module

import toothpick.config.Module
import com.example.vinciandroidv2.ui.global.mvp.list.ListContract
import com.example.vinciandroidv2.ui.global.mvp.list.ListPresenterImpl

class ListModule(listView: ListContract.View) : Module() {
    init {
        bind(ListContract.View::class.java).toInstance(listView)
        bind(ListContract.Presenter::class.java).to(ListPresenterImpl::class.java).singleton()
    }
}