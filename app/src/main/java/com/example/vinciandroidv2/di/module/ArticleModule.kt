package com.example.vinciandroidv2.di.module

import com.example.vinciandroidv2.ui.global.mvp.article.ArticleContract
import com.example.vinciandroidv2.ui.global.mvp.article.ArticlePresenterImpl
import toothpick.config.Module

class ArticleModule(articleView: ArticleContract.View) : Module() {
    init {
        bind(ArticleContract.View::class.java).toInstance(articleView)
        bind(ArticleContract.Presenter::class.java).to(ArticlePresenterImpl::class.java).singleton()
    }
}