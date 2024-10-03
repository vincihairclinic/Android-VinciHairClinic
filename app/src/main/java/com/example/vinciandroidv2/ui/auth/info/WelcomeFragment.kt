package com.example.vinciandroidv2.ui.auth.info

import android.content.Intent
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import com.bumptech.glide.Glide
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.di.module.UserModule
import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.helper.RealmHelper.copyFromRealm
import com.example.vinciandroidv2.network.respons.AppState
import com.example.vinciandroidv2.ui.Screens
import com.example.vinciandroidv2.ui.auth.questionnaire.ProcedureOfInterestFragment
import com.example.vinciandroidv2.ui.auth.questionnaire.QuestionnaireFlow
import com.example.vinciandroidv2.ui.auth.questionnaire.questionnaireFlow
import com.example.vinciandroidv2.ui.global.BaseFragment
import com.example.vinciandroidv2.ui.global.dp
import com.example.vinciandroidv2.ui.global.mvp.user.UserContract
import com.example.vinciandroidv2.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_email_verification.*
import toothpick.Scope
import toothpick.ktp.delegate.inject

enum class WelcomeButtonType(val resId: Int) { NO(R.id.negativeButton), YES(R.id.positiveButton) }

class WelcomeFragment : BaseFragment(), UserContract.View {
    override val layoutRes = R.layout.fragment_email_verification
    override val TAG = "WelcomeFragment"

    private val userPresenter: UserContract.Presenter by inject()
    override fun installModules(scope: Scope) {
        scope.installModules(UserModule(this))
    }

    private var buttonTypeClick: WelcomeButtonType? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (image?.layoutParams as ConstraintLayout.LayoutParams).apply {
            height = 200.dp
            width = 200.dp
        }
        Glide.with(this).load(R.drawable.group_7).into(image)

        negativeButton?.isVisible = true
        findButton(R.id.negativeButton)?.apply {
            setText("", R.color.text_white_FFFFFF, R.font.montserrat_semi_bold)
            setBackgroundColor(R.color.primary_brown_7D5F2B)
            onClick {
                buttonTypeClick = WelcomeButtonType.NO
                RealmHelper.getUserData()?.copyFromRealm()?.let {

                    if (questionnaireFlow == QuestionnaireFlow.AUTH) {
                        it.appState = AppState.COMPLETED_STATE.value
                    }

                    userPresenter.editUser(it)
                }
            }
        }
        findButton(R.id.positiveButton)?.apply {
            setText("", R.color.primary_brown_7D5F2B, R.font.montserrat_semi_bold)
            setBackgroundColor(R.color.background_white_FFFFFF)
            onClick {
                buttonTypeClick = WelcomeButtonType.YES
                RealmHelper.getUserData()?.copyFromRealm()?.let {

                    if (questionnaireFlow == QuestionnaireFlow.AUTH) {
                        it.appState = AppState.NUMBER_STATE.value
                    }

                    userPresenter.editUser(it)
                }
            }
        }
    }

    override fun setLocalization() {
        super.setLocalization()
        titleField?.text = RealmHelper.getLocalizedText("welcome.title.text")
        subTitleField?.text = RealmHelper.getLocalizedText("welcome.subtitle.text")
        findButton(R.id.negativeButton)?.setText(RealmHelper.getLocalizedText("welcome.button.no.text"))
        findButton(R.id.positiveButton)?.setText(RealmHelper.getLocalizedText("welcome.button.yes.text"))
    }

    override fun userLoaded() {
//        when (buttonTypeClick) {
//            WelcomeButtonType.NO -> {
//                startActivity(Intent(activity, MainActivity::class.java))
//                activity?.finish()
//            }
//            WelcomeButtonType.YES -> replace(
//                R.id.content,
//                Screens.Auth.getYourNumberFragment()
//            )
//        }

        when (questionnaireFlow) {
            QuestionnaireFlow.AUTH -> {

                when (buttonTypeClick) {
                    WelcomeButtonType.NO -> {
                        startActivity(Intent(activity, MainActivity::class.java))
                        activity?.finish()
                    }
                    WelcomeButtonType.YES -> replace(
                        android.R.id.content,
                        Screens.Auth.getYourNumberFragment()
                    )
                }
            }

//            QuestionnaireFlow.PROFILE -> {
//                activity?.supportFragmentManager?.popBackStack(
//                    ProcedureOfInterestFragment().TAG, POP_BACK_STACK_INCLUSIVE
//                )
//
//            }
            else ->{
                when (buttonTypeClick) {
                    WelcomeButtonType.NO -> {
                        startActivity(Intent(activity, MainActivity::class.java))
                        activity?.finish()
                    }
                    WelcomeButtonType.YES -> replace(
                        android.R.id.content,
                        Screens.Auth.getYourNumberFragment()
                    )
                }
            }
        }
    }

    override fun startLoading() {
        super<BaseFragment>.startLoading()
        findButton(buttonTypeClick?.resId ?: return)?.startLoading()
    }

    override fun stopLoading() {
        super<BaseFragment>.stopLoading()
        findButton(buttonTypeClick?.resId ?: return)?.stopLoading()
    }
}