package com.example.vinciandroidv2.di.module

import com.example.vinciandroidv2.ui.global.mvp.product.ProductContract
import com.example.vinciandroidv2.ui.global.mvp.product.ProductPresenterImpl
import toothpick.config.Module

class ProductModule(productView: ProductContract.View) : Module() {
    init {
        bind(ProductContract.View::class.java).toInstance(productView)
        bind(ProductContract.Presenter::class.java).to(ProductPresenterImpl::class.java).singleton()
    }
}