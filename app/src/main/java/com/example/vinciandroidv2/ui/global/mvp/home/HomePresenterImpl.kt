package com.example.vinciandroidv2.ui.global.mvp.home

import com.example.vinciandroidv2.network.ApiService
import com.example.vinciandroidv2.network.ResponseHandler
import com.example.vinciandroidv2.network.respons.Base
import com.example.vinciandroidv2.network.respons.BaseHome
import com.example.vinciandroidv2.ui.global.API_INTERNET_ERROR
import com.example.vinciandroidv2.ui.global.API_SERVER_ERROR
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import toothpick.InjectConstructor

@InjectConstructor
class HomePresenterImpl(
    private val view: HomeContract.View,
    private val apiService: ApiService
) : HomeContract.Presenter {
    override fun getHome() {
        apiService.getHome()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .doOnSubscribe { view.startLoading() }
            .doOnSuccess { view.stopLoading() }
            .doOnError { view.stopLoading() }
            .subscribe(object : ResponseHandler<BaseHome>() {
                override fun onSuccess(response: BaseHome) {
                    view.homeLoaded(response)
                }

                override fun noInternet() {
                    view.apiError(API_INTERNET_ERROR) { getHome() }
                }

                override fun apiException(apiException: Base) {
                    view.apiError(API_SERVER_ERROR, apiException, null)
                }
            })
    }
}
