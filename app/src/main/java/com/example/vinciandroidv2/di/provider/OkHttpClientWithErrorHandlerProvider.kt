package com.example.vinciandroidv2.di.provider

import okhttp3.OkHttpClient
import javax.inject.Inject
import javax.inject.Provider

class OkHttpClientWithErrorHandlerProvider @Inject constructor(
    private val client: OkHttpClient
) : Provider<OkHttpClient> {

    override fun get(): OkHttpClient = client
        .newBuilder()
        .build()
}