package com.example.vinciandroidv2.ui.global.mvp.product

import com.example.vinciandroidv2.network.ApiService
import com.example.vinciandroidv2.network.ResponseHandler
import com.example.vinciandroidv2.network.respons.Base
import com.example.vinciandroidv2.network.respons.BaseProduct
import com.example.vinciandroidv2.ui.global.API_INTERNET_ERROR
import com.example.vinciandroidv2.ui.global.API_SERVER_ERROR
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import toothpick.InjectConstructor

@InjectConstructor
class ProductPresenterImpl(
    private val view: ProductContract.View,
    private val apiService: ApiService,
) : ProductContract.Presenter {
    override fun getProductById(productId: Int) {
        apiService.getProductById(productId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .doOnSubscribe { view.startLoading() }
            .doOnSuccess { view.stopLoading() }
            .doOnError { view.stopLoading() }
            .subscribe(object : ResponseHandler<BaseProduct>() {
                override fun onSuccess(response: BaseProduct) {
                    view.productByIdLoaded(response)
                }

                override fun noInternet() {
                    view.apiError(API_INTERNET_ERROR) { getProductById(productId) }
                }

                override fun apiException(apiException: Base) {
                    view.apiError(API_SERVER_ERROR, apiException, null)
                }
            })
    }

    override fun getProductList(page: Int) {
        apiService.getProductList(page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .doOnSubscribe { view.startLoading() }
            .doOnSuccess { view.stopLoading() }
            .doOnError { view.stopLoading() }
            .subscribe(object : ResponseHandler<BaseProduct>() {
                override fun onSuccess(response: BaseProduct) {
                    view.productListByCategoryLoaded(null, response)
                }

                override fun noInternet() {
                    view.apiError(API_INTERNET_ERROR) { getProductList(page) }
                }

                override fun apiException(apiException: Base) {
                    view.apiError(API_SERVER_ERROR, apiException, null)
                }
            })
    }

    override fun getProductListByCategory(categoryId: Int, page: Int) {
        apiService.getProductListByCategory(categoryId, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .doOnSubscribe { view.startLoading() }
            .doOnSuccess { view.stopLoading() }
            .doOnError { view.stopLoading() }
            .subscribe(object : ResponseHandler<BaseProduct>() {
                override fun onSuccess(response: BaseProduct) {
                    view.productListByCategoryLoaded(categoryId, response)
                }

                override fun noInternet() {
                    view.apiError(API_INTERNET_ERROR) { getProductListByCategory(categoryId, page) }
                }

                override fun apiException(apiException: Base) {
                    view.apiError(API_SERVER_ERROR, apiException, null)
                }
            })
    }

    override fun getProductCategoryList() {
        apiService.getProductCategoryList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .doOnSubscribe { view.startLoading() }
            .doOnSuccess { view.stopLoading() }
            .doOnError { view.stopLoading() }
            .subscribe(object : ResponseHandler<BaseProduct>() {
                override fun onSuccess(response: BaseProduct) {
                    view.productCategoryListLoaded(response)
                }

                override fun noInternet() {
                    view.apiError(API_INTERNET_ERROR) { getProductCategoryList() }
                }

                override fun apiException(apiException: Base) {
                    view.apiError(API_SERVER_ERROR, apiException, null)
                }
            })
    }
}