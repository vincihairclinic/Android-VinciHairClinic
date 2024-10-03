package com.example.vinciandroidv2.ui.global.mvp.article

import com.example.vinciandroidv2.network.ApiService
import com.example.vinciandroidv2.network.ResponseHandler
import com.example.vinciandroidv2.network.respons.Base
import com.example.vinciandroidv2.network.respons.BaseArticle
import com.example.vinciandroidv2.ui.global.API_INTERNET_ERROR
import com.example.vinciandroidv2.ui.global.API_SERVER_ERROR
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import toothpick.InjectConstructor

@InjectConstructor
class ArticlePresenterImpl(
    private val view: ArticleContract.View,
    private val apiService: ApiService,
) : ArticleContract.Presenter {
    override fun getArticleById(articleId: Int) {
        apiService.getArticleById(articleId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .doOnSubscribe { view.startLoading() }
            .doOnSuccess { view.stopLoading() }
            .doOnError { view.stopLoading() }
            .subscribe(object : ResponseHandler<BaseArticle>() {
                override fun onSuccess(response: BaseArticle) {
                    view.articleByIdLoaded(response)
                }

                override fun noInternet() {
                    view.apiError(API_INTERNET_ERROR) { getArticleById(articleId) }
                }

                override fun apiException(apiException: Base) {
                    view.apiError(API_SERVER_ERROR, apiException, null)
                }
            })
    }

    override fun getArticleList(page: Int) {
        apiService.getArticleList(page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .doOnSubscribe { view.startLoading() }
            .doOnSuccess { view.stopLoading() }
            .doOnError { view.stopLoading() }
            .subscribe(object : ResponseHandler<BaseArticle>() {
                override fun onSuccess(response: BaseArticle) {
                    view.articleListByCategoryLoaded(response)
                }

                override fun noInternet() {
                    view.apiError(API_INTERNET_ERROR) { getArticleList(page) }
                }

                override fun apiException(apiException: Base) {
                    view.apiError(API_SERVER_ERROR, apiException, null)
                }
            })
    }

    override fun getArticleListByCategory( page: Int) {
        apiService.getArticleListByCategory( page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .doOnSubscribe { view.startLoading() }
            .doOnSuccess { view.stopLoading() }
            .doOnError { view.stopLoading() }
            .subscribe(object : ResponseHandler<BaseArticle>() {
                override fun onSuccess(response: BaseArticle) {
                    view.articleListByCategoryLoaded(response)
                }

                override fun noInternet() {
                    view.apiError(API_INTERNET_ERROR) { getArticleListByCategory( page) }
                }

                override fun apiException(apiException: Base) {
                    view.apiError(API_SERVER_ERROR, apiException, null)
                }
            })
    }

    override fun getArticleCategoryList() {
        apiService.getArticleCategoryList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .doOnSubscribe { view.startLoading() }
            .doOnSuccess { view.stopLoading() }
            .doOnError { view.stopLoading() }
            .subscribe(object : ResponseHandler<BaseArticle>() {
                override fun onSuccess(response: BaseArticle) {
                    view.articleCategoryListLoaded(response)
                }

                override fun noInternet() {
                    view.apiError(API_INTERNET_ERROR) { getArticleCategoryList() }
                }

                override fun apiException(apiException: Base) {
                    view.apiError(API_SERVER_ERROR, apiException, null)
                }
            })
    }

    override fun getNextArticleListAfterArticleId(currentArticleId: Int, page: Int) {
        apiService.getNextArticleListAfterArticleId(currentArticleId, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .doOnSubscribe { view.startLoading() }
            .doOnSuccess { view.stopLoading() }
            .doOnError { view.stopLoading() }
            .subscribe(object : ResponseHandler<BaseArticle>() {
                override fun onSuccess(response: BaseArticle) {
                    view.nextArticleListLoaded(response)
                }

                override fun noInternet() {
                    view.apiError(API_INTERNET_ERROR) {
                        getNextArticleListAfterArticleId(currentArticleId, page)
                    }
                }

                override fun apiException(apiException: Base) {
                    view.apiError(API_SERVER_ERROR, apiException, null)
                }
            })
    }

    override fun getPodcastList(page: Int) {
        apiService.getPodcastList( page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .doOnSubscribe { view.startLoading() }
            .doOnSuccess { view.stopLoading() }
            .doOnError { view.stopLoading() }
            .subscribe(object : ResponseHandler<BaseArticle>() {
                override fun onSuccess(response: BaseArticle) {
                    view.articleListByCategoryLoaded(response)
                }

                override fun noInternet() {
                    view.apiError(API_INTERNET_ERROR) { getPodcastList( page) }
                }

                override fun apiException(apiException: Base) {
                    view.apiError(API_SERVER_ERROR, apiException, null)
                }
            })
    }

    override fun getPodcastByID(id: Int) {
        apiService.getPodcastByID( id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .doOnSubscribe { view.startLoading() }
            .doOnSuccess { view.stopLoading() }
            .doOnError { view.stopLoading() }
            .subscribe(object : ResponseHandler<BaseArticle>() {
                override fun onSuccess(response: BaseArticle) {
                    view.articleListByCategoryLoaded(response)
                }

                override fun noInternet() {
                    view.apiError(API_INTERNET_ERROR) { getPodcastByID( id) }
                }

                override fun apiException(apiException: Base) {
                    view.apiError(API_SERVER_ERROR, apiException, null)
                }
            })
    }


    override fun getYoutubeList(page: Int) {
        apiService.getYoutubeList(page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .doOnSubscribe { view.startLoading() }
            .doOnSuccess { view.stopLoading() }
            .doOnError { view.stopLoading() }
            .subscribe(object : ResponseHandler<BaseArticle>() {
                override fun onSuccess(response: BaseArticle) {
                    view.showVideos(response.videos)
                }

                override fun noInternet() {
                    view.apiError(API_INTERNET_ERROR) { getYoutubeList(page) }
                }

                override fun apiException(apiException: Base) {
                    view.apiError(API_SERVER_ERROR, apiException, null)
                }
            })
    }
}