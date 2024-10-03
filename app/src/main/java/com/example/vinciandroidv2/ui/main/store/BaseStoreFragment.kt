package com.example.vinciandroidv2.ui.main.store

import com.example.vinciandroidv2.di.module.ProductModule
import com.example.vinciandroidv2.ui.global.BaseFragment
import com.example.vinciandroidv2.ui.global.mvp.product.ProductContract
import toothpick.Scope
import toothpick.ktp.delegate.inject

abstract class BaseStoreFragment : BaseFragment(), ProductContract.View {
    internal val productPresenter: ProductContract.Presenter by inject()
    override fun installModules(scope: Scope) {
        scope.installModules(ProductModule(this))
    }
}