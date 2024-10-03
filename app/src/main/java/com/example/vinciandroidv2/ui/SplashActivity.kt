package com.example.vinciandroidv2.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.example.vinciandroidv2.App
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.di.module.ListModule
import com.example.vinciandroidv2.di.module.UserModule
import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.network.respons.AppState
import com.example.vinciandroidv2.network.respons.Token
import com.example.vinciandroidv2.ui.auth.AuthActivity
import com.example.vinciandroidv2.ui.global.BaseActivity
import com.example.vinciandroidv2.ui.global.host
import com.example.vinciandroidv2.ui.global.mvp.list.ListContract
import com.example.vinciandroidv2.ui.global.mvp.user.UserContract
import com.example.vinciandroidv2.ui.global.userToken
import com.example.vinciandroidv2.ui.localization.LocalizationActivity
import com.example.vinciandroidv2.ui.main.MainActivity
import toothpick.Scope
import toothpick.ktp.delegate.inject

class SplashActivity : BaseActivity(), ListContract.View, UserContract.View {
    override val layoutRes = R.layout.activity_splash

    private val listPresenter: ListContract.Presenter by inject()
    private val userPresenter: UserContract.Presenter by inject()
    override fun installModules(scope: Scope) {
        scope.installModules(ListModule(this), UserModule(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        listPresenter.getLists()
    }

    override fun listsLoaded() {
        listPresenter.getLocalization()
    }

    override fun localizationsLoaded() {
        val token = RealmHelper.realm?.where(Token::class.java)?.findFirst()
        userToken = "${token?.tokenType} ${token?.accessToken}"

        if (null == token?.accessToken) {
            startActivity(Intent(this, LocalizationActivity::class.java))
            finish()
        } else {
            RealmHelper.getUserData()?.countryId?.let { countryId ->
                host = RealmHelper.getHostFromSelectedCountry(countryId)
                (application as App).updateAppScope()
            }
            listPresenter.getListsAfterLogin()
        }
    }

    override fun listsAfterLoginLoaded() {
//        RealmHelper.getUserData()?.appState.let {
//            if (it != AppState.COMPLETED_STATE.value) {
//                startActivity(Intent(this, AuthActivity::class.java))
//                finish()
//            } else userPresenter.getUser()
//        }
        userPresenter.getUser()
    }

    override fun userLoaded() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}