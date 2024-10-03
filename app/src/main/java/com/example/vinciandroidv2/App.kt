package com.example.vinciandroidv2

import android.annotation.SuppressLint
import android.app.Application
import android.net.*
import androidx.appcompat.app.AppCompatDelegate
import com.example.vinciandroidv2.di.DI
import com.example.vinciandroidv2.di.module.AppModule
import com.example.vinciandroidv2.model.NetworkStateEvent
import io.realm.BuildConfig
import io.realm.Realm
import io.realm.RealmConfiguration
import org.greenrobot.eventbus.EventBus
import toothpick.Scope
import toothpick.configuration.Configuration
import toothpick.ktp.KTP

class App : Application() {
    lateinit var scope: Scope

    private val request: NetworkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .build()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        // network is available for use
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            EventBus.getDefault().post(NetworkStateEvent().apply { isConnected = true })
        }

        // Network capabilities have changed for the network
        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            val unmetered =
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
        }

        // lost network connection
        override fun onLost(network: Network) {
            super.onLost(network)
            EventBus.getDefault().post(NetworkStateEvent().apply { isConnected = false })
        }

        override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
            super.onLinkPropertiesChanged(network, linkProperties)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        initToothpick()
        initRealm()
        initAppScope()

        (getSystemService(ConnectivityManager::class.java) as ConnectivityManager)
//            .requestNetwork(request, networkCallback)
    }

    private fun initToothpick() {
        if (BuildConfig.DEBUG) {
            KTP.setConfiguration(Configuration.forDevelopment().preventMultipleRootScopes())
        } else {
            KTP.setConfiguration(Configuration.forProduction())
        }
    }

    private fun initRealm() {
        Realm.init(this)
        val mRealmConfiguration = RealmConfiguration.Builder()
            .name(BuildConfig.LIBRARY_PACKAGE_NAME)
            .schemaVersion(2)
            .deleteRealmIfMigrationNeeded()
            .allowWritesOnUiThread(true)
            .build()
        Realm.getInstance(mRealmConfiguration)
        Realm.setDefaultConfiguration(mRealmConfiguration)
    }

    fun initAppScope() {
        scope = KTP.openScope(DI.APP_SCOPE)
            .installModules(AppModule(this))
    }

    fun updateAppScope() {
        KTP.closeScope(scope.name)
        initAppScope()
    }

    override fun onTerminate() {
        super.onTerminate()
        KTP.closeScope(scope.name)
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
    }
}