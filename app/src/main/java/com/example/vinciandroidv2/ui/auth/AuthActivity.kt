package com.example.vinciandroidv2.ui.auth

import android.os.Bundle
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.network.respons.AppState
import com.example.vinciandroidv2.ui.Screens
import com.example.vinciandroidv2.ui.auth.info.EmailVerificationFragment
import com.example.vinciandroidv2.ui.auth.info.GetBackFragment
import com.example.vinciandroidv2.ui.auth.info.WelcomeFragment
import com.example.vinciandroidv2.ui.auth.onboarding.OnBoardingFragment
import com.example.vinciandroidv2.ui.auth.questionnaire.*
import com.example.vinciandroidv2.ui.auth.questionnaire2.YourNumberFragment
import com.example.vinciandroidv2.ui.global.BaseActivity

class AuthActivity : BaseActivity() {
    override val layoutRes = R.layout.activity_auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        replace(android.R.id.content, Screens.Auth.getOnBoardingFragment())
//        replace(R.id.content, Screens.Auth.getCreatePasswordFragment())
    }

    fun getDestinationOfCurrentAppState(userState: String?) = when (userState) {
        AppState.VERIFY_STATE.value -> Screens.Auth.getEmailVerificationFragment()
        AppState.GENDER_STATE.value -> Screens.Auth.getGenderAndAgeFragment()
        AppState.BACKGROUND_STATE.value -> Screens.Auth.getYourBackgroundFragment()
        AppState.PROCEDURE_STATE.value -> Screens.Auth.getProcedureOfInterestFragment()
        AppState.HAIR_LOSS_TYPE_STATE.value -> Screens.Auth.getHairLossTypeFragment()
        AppState.CLINIC_STATE.value -> Screens.Auth.getPreferredClinicFragment()
        AppState.WELCOME_STATE.value -> Screens.Auth.getWelcomeFragment()
        AppState.NUMBER_STATE.value -> Screens.Auth.getYourNumberFragment()
        AppState.HAIR_PHOTOS_STATE.value -> Screens.Auth.getHairPhotosFragment()
        else -> Screens.Auth.getOnBoardingFragment()
    }

    override fun onBackPressed() {
        if (1 == supportFragmentManager.backStackEntryCount) finish()
        when (supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount - 1).name) {
            OnBoardingFragment().TAG -> finish()
            EmailVerificationFragment().TAG -> return
            GenderAndAgeFragment().TAG -> finish()
            WelcomeFragment().TAG -> return
            GetBackFragment().TAG -> return
            else -> supportFragmentManager.popBackStack()
        }
    }
}