package com.example.vinciandroidv2.di.provider

import android.content.Context
import com.example.vinciandroidv2.ui.global.userToken
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Provider

class OkHttpClientProvider @Inject constructor(
    private val context: Context
) : Provider<OkHttpClient> {

    override fun get(): OkHttpClient = with(OkHttpClient.Builder()) {
        cache(Cache(context.cacheDir, CACHE_SIZE_BYTES))
        connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        readTimeout(TIMEOUT, TimeUnit.SECONDS)
        addNetworkInterceptor(
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        )
        addInterceptor(AuthInterceptor())
        build()
    }

    inner class AuthInterceptor : Interceptor {

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val original = chain.request()
            val requestBuilder = original.newBuilder()
            if (!userToken.isNullOrEmpty()) {
                requestBuilder.addHeader("Authorization", userToken ?: "")
            }
            requestBuilder.method(original.method, original.body)
            return chain.proceed(requestBuilder.build())
        }
    }

    companion object {
        private const val CACHE_SIZE_BYTES = 20 * 1024L
        private const val TIMEOUT = 30L
    }
}
