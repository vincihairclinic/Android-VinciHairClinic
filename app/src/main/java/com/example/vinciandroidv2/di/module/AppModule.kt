package com.example.vinciandroidv2.di.module

import android.content.Context
import com.example.vinciandroidv2.di.ServerPath
import com.example.vinciandroidv2.di.WithErrorHandler
import com.google.gson.Gson
import com.example.vinciandroidv2.di.provider.ApiProvider
import com.example.vinciandroidv2.di.provider.GsonProvider
import com.example.vinciandroidv2.di.provider.OkHttpClientProvider
import com.example.vinciandroidv2.di.provider.OkHttpClientWithErrorHandlerProvider
import com.example.vinciandroidv2.network.ApiService
import com.example.vinciandroidv2.network.ResponseHandler
import com.example.vinciandroidv2.ui.global.baseUrl
import io.realm.Realm
import okhttp3.OkHttpClient
import toothpick.config.Module

class AppModule(context: Context) : Module() {
    init {
        bind(Context::class.java).toInstance(context)
        bind(Realm::class.java).toInstance(Realm.getDefaultInstance())
        bind(Gson::class.java).toProvider(GsonProvider::class.java).providesSingleton()
        bind(String::class.java).withName(ServerPath::class.java).toInstance(baseUrl)
        bind(OkHttpClient::class.java).toProvider(OkHttpClientProvider::class.java)
            .providesSingleton()
        bind(OkHttpClient::class.java).withName(WithErrorHandler::class.java)
            .toProvider(OkHttpClientWithErrorHandlerProvider::class.java)
            .providesSingleton()
        bind(ApiService::class.java).toProvider(ApiProvider::class.java).providesSingleton()
        bind(ResponseHandler::class.java).singleton()
    }
}