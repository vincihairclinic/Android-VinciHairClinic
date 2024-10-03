package com.example.vinciandroidv2.di.provider

import com.example.vinciandroidv2.di.ServerPath
import com.example.vinciandroidv2.di.WithErrorHandler
import com.example.vinciandroidv2.network.ApiService
import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Provider

class ApiProvider @Inject constructor(
    @WithErrorHandler private val okHttpClient: OkHttpClient,
    private val gson: Gson,
    @ServerPath private val serverPath: String
) : Provider<ApiService> {

    override fun get(): ApiService = getOriginalApi()

    private fun getOriginalApi() =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .baseUrl(serverPath)
            .build()
            .create(ApiService::class.java)
}

//@InjectConstructor
//class ApiProvider(
//    @WithErrorHandler private val okHttpClient: OkHttpClient,
//    private val gson: Gson,
//    @ServerPath private val serverPath: String
//) : Provider<ApiService> {
//
//    override fun get(): ApiService = getOriginalApi()
//
//    private fun getOriginalApi() =
//        Retrofit.Builder()
//            .addConverterFactory(GsonConverterFactory.create(gson))
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//            .client(okHttpClient)
//            .baseUrl(serverPath)
//            .build()
//            .create(ApiService::class.java)
//}

