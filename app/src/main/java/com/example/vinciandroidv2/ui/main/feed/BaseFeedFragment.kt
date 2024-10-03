package com.example.vinciandroidv2.ui.main.feed

import com.example.vinciandroidv2.di.module.ArticleModule
import com.example.vinciandroidv2.ui.global.BaseFragment
import com.example.vinciandroidv2.ui.global.mvp.article.ArticleContract
import toothpick.Scope
import toothpick.ktp.delegate.inject

abstract class BaseFeedFragment : BaseFragment(), ArticleContract.View {
    internal val articlePresenter: ArticleContract.Presenter by inject()
    override fun installModules(scope: Scope) {
        scope.installModules(ArticleModule(this))
    }
}