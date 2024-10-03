package com.example.vinciandroidv2.network

import com.example.vinciandroidv2.model.LogoutEvent
import com.google.gson.Gson
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import retrofit2.HttpException
import com.example.vinciandroidv2.network.respons.Base
import org.greenrobot.eventbus.EventBus
import java.net.SocketTimeoutException
import java.net.UnknownHostException

abstract class ResponseHandler<T> : SingleObserver<T> {
    private val gson: Gson = Gson()

    override fun onSuccess(response: T) {

    }

    override fun onSubscribe(d: Disposable) {
    }

    override fun onError(e: Throwable) {
        when (e) {
            is HttpException -> {
                try {
                    val body = e.response()?.errorBody()
                    val code = e.response()?.code()
                    if (403 == code) {
                        EventBus.getDefault().post(LogoutEvent())
                        return
                    }
                    val adapter = gson.getAdapter(Base::class.java)
                    val errorParser = adapter.fromJson(body?.string())
                    errorParser?.code = code
                    errorParser?.let {
                        apiException(it)
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
            is SocketTimeoutException -> {
                try {
                    noInternet()
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
            is UnknownHostException -> {
                try {
                    noInternet()
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
            else -> {
                e.printStackTrace()
            }
        }
    }

    open fun logout() {}

    open fun noInternet() {}

    open fun retry() {}

    open fun apiException(apiException: Base) {}

}