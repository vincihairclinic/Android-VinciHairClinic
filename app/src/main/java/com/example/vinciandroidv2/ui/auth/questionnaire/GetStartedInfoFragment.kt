package com.example.vinciandroidv2.ui.auth.questionnaire

import android.content.Intent
import android.os.Bundle
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.di.module.ListModule
import com.example.vinciandroidv2.di.module.UserModule
import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.helper.RealmHelper.copyFromRealm
import com.example.vinciandroidv2.ui.Screens
import com.example.vinciandroidv2.ui.global.BaseFragment
import com.example.vinciandroidv2.ui.global.dp
import com.example.vinciandroidv2.ui.global.mvp.list.ListContract
import com.example.vinciandroidv2.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_get_started_info.*
import toothpick.Scope
import toothpick.ktp.delegate.inject

class GetStartedInfoFragment : BaseFragment(), ListContract.View {
    override val layoutRes = R.layout.fragment_get_started_info
    override val TAG = "GetStartedInfoFragment"
    private val listPresenter: ListContract.Presenter by inject()
    override fun installModules(scope: Scope) {
        scope.installModules(ListModule(this))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        listPresenter.getListsAfterLogin()
        findButton(R.id.scipButton)?.apply {
            setText("", R.color.text_white_FFFFFF, R.font.montserrat_semi_bold)
            setBackgroundColor(R.color.primary_brown_7D5F2B)
            onClick {
                startActivity(Intent(activity, MainActivity::class.java))
                activity?.finish()
            }
        }
        findButton(R.id.continueButton)?.apply {
            setText("", R.color.primary_dark_brown_503629, R.font.montserrat_semi_bold)
            setBackgroundColor(R.color.background_white_FFFFFF)
            onClick {
                replace(android.R.id.content, Screens.Auth.getGenderAndAgeFragment())
            }
        }
    }

    override fun setLocalization() {
        findButton(R.id.scipButton)?.setText(RealmHelper.getLocalizedText("auth.skip"))
        findButton(R.id.continueButton)?.setText(RealmHelper.getLocalizedText("emailverification.button.continue.text"))
        textInfo?.text = RealmHelper.getLocalizedText("getstarted.maintext")
    }

}