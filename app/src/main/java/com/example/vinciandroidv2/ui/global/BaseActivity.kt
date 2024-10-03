package com.example.vinciandroidv2.ui.global

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.vinciandroidv2.App
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.di.DI
import com.example.vinciandroidv2.helper.AlertListener
import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.helper.showBaseAlert
import com.example.vinciandroidv2.lifecycle.LifecycleObserver
import com.example.vinciandroidv2.model.*
import com.example.vinciandroidv2.network.respons.Base
import com.example.vinciandroidv2.ui.SplashActivity
import com.example.vinciandroidv2.ui.global.mvp.BaseView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import io.realm.Realm
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import toothpick.Scope
import toothpick.Toothpick
import toothpick.ktp.KTP
import toothpick.ktp.delegate.inject

abstract class BaseActivity : AppCompatActivity(), LifecycleObserver, BaseView {
    abstract val layoutRes: Int

    //    protected val apiService: ApiService by inject()
    protected val realm: Realm by inject()
    protected open val parentScopeName: String = DI.APP_SCOPE
    protected lateinit var scope: Scope
        private set

    private val delegate = object : LayoutChangeListener.Delegate {
        private val uiHandler = Handler(Looper.getMainLooper())

        override fun layoutDidChange(oldHeight: Int, newHeight: Int, tempBottom: Int) {
            uiHandler.post {
                if (tempBottom <= resources?.getDimension(R.dimen.navigation_height)?.toInt() ?: 82
                ) {
                    return@post
                }
                keyboardStateChanged(newHeight > oldHeight)
            }
        }
    }

    lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initActivityScope()
        val view = LayoutInflater.from(this).inflate(layoutRes, null)
        setContentView(view)
        view?.addOnLayoutChangeListener(LayoutChangeListener().also {
            it.delegate = delegate
        })
        lifecycle.addObserver(this)
        hideKeyboard()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        supportFragmentManager.addOnBackStackChangedListener {
            for (i in 0 until supportFragmentManager.backStackEntryCount) {
                println(supportFragmentManager.getBackStackEntryAt(i).name.toString())
            }
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    private fun initActivityScope() {
        scope = createScope()
        installModules(scope)
        Toothpick.inject(this, scope)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onMessageEvent(event: LogoutEvent) {
        logOutUser()
    }

    internal fun logOutUser() {
        RealmHelper.removeSensitiveDataFromBase()
        (application as App).updateAppScope()
        startActivity(Intent(this, SplashActivity::class.java))
        finish()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onMessageEvent(event: NetworkStateEvent) {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onMessageEvent(event: LocalizationEvent) {
        setLocalization()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onMessageEvent(event: UpdateActivityScopeEvent) {
        KTP.closeScope(scope.name)
        initActivityScope()
        EventBus.getDefault().post(UpdateFragmentScopeEvent())
    }

    protected open fun createScope(): Scope {
        return Toothpick.openScopes(parentScopeName, objectScopeName())
    }

    protected open fun installModules(scope: Scope) {}

    protected fun replace(
        @IdRes hostId: Int,
        fragment: BaseFragment,
        isAddToBackStack: Boolean = true
    ) {
        val trans = supportFragmentManager.beginTransaction()
            .replace(hostId, fragment, fragment.TAG)
        if (isAddToBackStack) {
            trans.addToBackStack(fragment.TAG)
        }
        trans.commit()
    }

    protected fun findFragment(fragmentTag: String): Fragment? =
        supportFragmentManager.findFragmentByTag(fragmentTag)

    private fun hideKeyboard() {
        try {
            (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(window?.decorView?.windowToken, 0)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    open fun keyboardStateChanged(isShown: Boolean) {}

    fun setStatusBarColor(@ColorRes colorId: Int) {
        window.statusBarColor = resources.getColor(colorId)
    }

    override fun connectListener() {
        setLocalization()
    }

    override fun disconnectListener() {
        hideKeyboard()
    }

    open fun setLocalization() {}

    override fun apiError(error: String, apiException: Base?, function: (() -> Unit)?) {
        val listener = object : AlertListener {
            override fun actionPositive() {
                function?.invoke()
            }

            override fun actionNegative() {}
        }
        when (error) {
            API_INTERNET_ERROR -> showBaseAlert(
                this,
                title = apiException?.title,
                subtitle = getString(R.string.check_internet_connection),
                positiveText = getString(R.string.re_try),
                alertListener = listener
            )
            API_SERVER_ERROR -> showBaseAlert(
                this,
                title = apiException?.title,
                subtitle = apiException?.message,
                positiveText = getString(R.string.ok_label),
                alertListener = listener
            )
        }
    }
}