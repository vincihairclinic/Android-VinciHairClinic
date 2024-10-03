package com.example.vinciandroidv2.ui.global.mvp.user

import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.network.ApiService
import com.example.vinciandroidv2.network.ResponseHandler
import com.example.vinciandroidv2.network.respons.*
import com.example.vinciandroidv2.ui.global.API_INTERNET_ERROR
import com.example.vinciandroidv2.ui.global.API_SERVER_ERROR
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.RealmObject
import toothpick.InjectConstructor

@InjectConstructor
class UserPresenterImpl(
    private val view: UserContract.View,
    private val apiService: ApiService,
) : UserContract.Presenter {
    override fun getUser() {
        apiService.getUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .doOnSubscribe { view.startLoading() }
            .doOnSuccess { view.stopLoading() }
            .doOnError { view.stopLoading() }
            .subscribe(object : ResponseHandler<BaseUser>() {
                override fun onSuccess(response: BaseUser) {
                    RealmHelper.realm?.where(User::class.java)?.findAll()?.let {
                        val temp = ArrayList<RealmObject>()
                        temp.addAll(it)
                        RealmHelper.remove(temp)
                    }
                    response.user?.let {
                        RealmHelper.saveObject(it)
                    }
                    view.userLoaded()
                }

                override fun noInternet() {
                    view.apiError(API_INTERNET_ERROR) { getUser() }
                }

                override fun apiException(apiException: Base) {
                    view.apiError(API_SERVER_ERROR, apiException, null)
                }
            })
    }

    override fun editUser(user: User) {
        apiService.editUser(user)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .doOnSubscribe { view.startLoading() }
            .doOnSuccess { view.stopLoading() }
            .doOnError { view.stopLoading() }
            .subscribe(object : ResponseHandler<Any>() {
                override fun onSuccess(response: Any) {
                    getUser()
                }

                override fun noInternet() {
                    view.apiError(API_INTERNET_ERROR) { editUser(user) }
                }

                override fun apiException(apiException: Base) {
                    view.apiError(API_SERVER_ERROR, apiException, null)
                }
            })
    }

    override fun editUserProcedureIdList(userProcedureIdList: UserProcedureIdList) {
        apiService.editUserProcedureIdList(userProcedureIdList)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .doOnSubscribe { view.startLoading() }
            .doOnSuccess { view.stopLoading() }
            .doOnError { view.stopLoading() }
            .subscribe(object : ResponseHandler<Any>() {
                override fun onSuccess(response: Any) {
                    getUser()
                }

                override fun noInternet() {
                    view.apiError(API_INTERNET_ERROR) { editUserProcedureIdList(userProcedureIdList) }
                }

                override fun apiException(apiException: Base) {
                    view.apiError(API_SERVER_ERROR, apiException, null)
                }
            })
    }

    override fun deleteAccount() {
        apiService.deleteAccount()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .doOnSubscribe { view.startLoading() }
            .doOnSuccess { view.stopLoading() }
            .doOnError { view.stopLoading() }
            .subscribe(object : ResponseHandler<Any>() {
                override fun onSuccess(response: Any) {
                    view.accountDeleted()
                }

                override fun noInternet() {
                    view.apiError(API_INTERNET_ERROR) { deleteAccount() }
                }

                override fun apiException(apiException: Base) {
                    view.apiError(API_SERVER_ERROR, apiException, null)
                }
            })
    }

    override fun simulationRequestsCreate(simulation: Simulation) {
        apiService.simulationRequestsCreate(simulation)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .doOnSubscribe { view.startLoading() }
            .doOnSuccess { view.stopLoading() }
            .doOnError { view.stopLoading() }
            .subscribe(object : ResponseHandler<Any>() {
                override fun onSuccess(response: Any) {
                    view.simulationRequestsDone()
                }

                override fun noInternet() {
                    view.apiError(API_INTERNET_ERROR) { simulationRequestsCreate(simulation) }
                }

                override fun apiException(apiException: Base) {
                    view.apiError(API_SERVER_ERROR, apiException, null)
                }
            })

    }
}