package com.example.vinciandroidv2.ui.auth.onboarding

import android.os.Bundle
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.di.module.AuthModule
import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.helper.RealmHelper.copyFromRealm
import com.example.vinciandroidv2.ui.Screens
import com.example.vinciandroidv2.ui.global.*
import com.example.vinciandroidv2.ui.global.mvp.auth.AuthContract
import kotlinx.android.synthetic.main.fragment_on_boarding.*
import toothpick.Scope
import toothpick.ktp.delegate.inject

class OnBoardingFragment : BaseFragment(), AuthContract.View {
    override val layoutRes = R.layout.fragment_on_boarding
    override val TAG = "OnBoardingFragment"
    private val authPresenter: AuthContract.Presenter by inject()
    override fun installModules(scope: Scope) {
        scope.installModules(AuthModule(this))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        ViewPagerStateAdapter(
            this,
            arrayListOf(
                OnBoardingStepFragment(1),
                OnBoardingStepFragment(2),
                OnBoardingStepFragment(3)
            )
        ).initViewPager(viewPager, null)

        findButton(R.id.signInButton)?.apply {
            setText("", R.color.primary_dark_brown_503629, R.font.montserrat_semi_bold)
            setBackgroundColor(R.color.background_white_FFFFFF)
            setBorder(R.color.primary_dark_brown_503629, 1.dp)
            onClick {
                replace(android.R.id.content, Screens.Auth.getSignInFragment())
            }
        }

        findButton(R.id.getStartedButton)?.apply {
            setText("", R.color.text_white_FFFFFF, R.font.montserrat_semi_bold)
            setBackgroundColor(R.color.primary_brown_7D5F2B)
            onClick {
                authPresenter.autoRegister(selectedCountryId, selectedLanguageKey, "")
            }
        }
    }

    override fun userLoggedAuth() {
        super.userLoggedAuth()
        replace(android.R.id.content, Screens.Auth.getGetStartedInfoFragment())
    }

    override fun setLocalization() {
        super.setLocalization()
        findButton(R.id.signInButton)?.setText(RealmHelper.getLocalizedText("onboarding.button.signin.text"))
        findButton(R.id.getStartedButton)?.setText(RealmHelper.getLocalizedText("onboarding.button.getstarted.text"))
    }
}