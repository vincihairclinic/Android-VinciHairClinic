package com.example.vinciandroidv2.ui.global.mvp.auth

import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.network.ApiService
import com.example.vinciandroidv2.network.ResponseHandler
import com.example.vinciandroidv2.network.respons.Base
import com.example.vinciandroidv2.network.respons.Token
import com.example.vinciandroidv2.network.respons.User
import com.example.vinciandroidv2.ui.global.API_INTERNET_ERROR
import com.example.vinciandroidv2.ui.global.API_SERVER_ERROR
import com.example.vinciandroidv2.ui.global.sha256
import com.example.vinciandroidv2.ui.global.userToken
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import toothpick.InjectConstructor

@InjectConstructor
class AuthPresenterImpl(
    private val view: AuthContract.View,
    private val apiService: ApiService,
) : AuthContract.Presenter {
    override fun login(email: String, password: String) {
        apiService.login(email, password.sha256())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .doOnSubscribe { view.startLoading() }
            .doOnSuccess { view.stopLoading() }
            .doOnError { view.stopLoading() }
            .subscribe(object : ResponseHandler<Token>() {
                override fun onSuccess(response: Token) {
                    RealmHelper.saveObject(response)
                    userToken = "${response.tokenType ?: ""} ${response.accessToken ?: ""}"
                    view.userLoggedAuth()
                }

                override fun noInternet() {
                    view.apiError(API_INTERNET_ERROR) { login(email, password) }
                }

                override fun apiException(apiException: Base) {
                    view.apiError(API_SERVER_ERROR, apiException, null)
                }
            })
    }

    override fun register(user: User) {
        apiService.register(user.apply { password = password?.sha256() })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .doOnSubscribe { view.startLoading() }
            .doOnSuccess { view.stopLoading() }
            .doOnError { view.stopLoading() }
            .subscribe(object : ResponseHandler<Token>() {
                override fun onSuccess(response: Token) {
                    RealmHelper.saveObject(response)
                    userToken = "${response.tokenType ?: ""} ${response.accessToken ?: ""}"
                    view.userLoggedAuth()
                }

                override fun noInternet() {
                    view.apiError(API_INTERNET_ERROR) { register(user) }
                }

                override fun apiException(apiException: Base) {
                    view.apiError(API_SERVER_ERROR, apiException, null)
                }
            })
    }

    override fun forgotPassword(email: String) {
        apiService.forgotPassword(email)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .doOnSubscribe { view.startLoading() }
            .doOnSuccess { view.stopLoading() }
            .doOnError { view.stopLoading() }
            .subscribe(object : ResponseHandler<Any>() {
                override fun onSuccess(response: Any) {
                    view.passwordForgotRequestSent()
                }

                override fun noInternet() {
                    view.apiError(API_INTERNET_ERROR) { forgotPassword(email) }
                }

                override fun apiException(apiException: Base) {
                    view.apiError(API_SERVER_ERROR, apiException, null)
                }
            })
    }

    override fun changePassword(currentPassword: String, newPassword: String) {
        apiService.changePassword(currentPassword.sha256(), newPassword.sha256())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .doOnSubscribe { view.startLoading() }
            .doOnSuccess { view.stopLoading() }
            .doOnError { view.stopLoading() }
            .subscribe(object : ResponseHandler<Any>() {
                override fun onSuccess(response: Any) {
                    view.passwordChanged()
                }

                override fun noInternet() {
                    view.apiError(API_INTERNET_ERROR) {
                        changePassword(currentPassword, newPassword)
                    }
                }

                override fun apiException(apiException: Base) {
                    view.apiError(API_SERVER_ERROR, apiException, null)
                }
            })
    }

    override fun autoRegister(countryId: Int, languageKey: String, appState: String) {
        apiService.autoRegister(countryId, languageKey, appState)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .doOnSubscribe { view.startLoading() }
            .doOnSuccess { view.stopLoading() }
            .doOnError { view.stopLoading() }
            .subscribe(object : ResponseHandler<Token>() {
                override fun onSuccess(response: Token) {
                    RealmHelper.saveObject(response)
                    userToken = "${response.tokenType ?: ""} ${response.accessToken ?: ""}"
                    view.userLoggedAuth()
                    view.userLoggedAuth()
                }

                override fun noInternet() {
                    view.apiError(API_INTERNET_ERROR) { autoRegister(countryId, languageKey, appState) }
                }

                override fun apiException(apiException: Base) {
                    view.apiError(API_SERVER_ERROR, apiException, null)
                }
            })
    }
}