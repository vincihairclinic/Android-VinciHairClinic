package com.example.vinciandroidv2.ui.localization

import android.os.Bundle
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.ui.Screens
import com.example.vinciandroidv2.ui.global.BaseActivity

class LocalizationActivity : BaseActivity() {
    override val layoutRes = R.layout.activity_auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            replace(android.R.id.content, Screens.Localization.getCountrySelectionFragment())
    }

    override fun onBackPressed() {
        if (1 == supportFragmentManager.backStackEntryCount) finish()
        when (supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount - 1).name) {

            else -> supportFragmentManager.popBackStack()
        }
    }
}