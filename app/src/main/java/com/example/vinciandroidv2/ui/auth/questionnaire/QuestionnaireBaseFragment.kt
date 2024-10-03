package com.example.vinciandroidv2.ui.auth.questionnaire

import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.view.isVisible
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.di.module.UserModule
import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.ui.global.BaseFragment
import com.example.vinciandroidv2.ui.global.mvp.user.UserContract
import kotlinx.android.synthetic.main.fragment_sign_up_guestionnaire_base.*
import kotlinx.android.synthetic.main.view_next_button.view.*
import toothpick.Scope
import toothpick.ktp.delegate.inject

enum class QuestionnaireFlow { AUTH, HOME, PROFILE }

var questionnaireFlow: QuestionnaireFlow = QuestionnaireFlow.AUTH

abstract class QuestionnaireBaseFragment : BaseFragment(), UserContract.View {
    abstract val includedLayoutRes: Int
    abstract val progress: Int

    override val layoutRes = R.layout.fragment_sign_up_guestionnaire_base

    internal val userPresenter: UserContract.Presenter by inject()
    override fun installModules(scope: Scope) {
        scope.installModules(UserModule(this))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        closeButton?.setOnClickListener { activity?.onBackPressed() }
        backButton?.setOnClickListener { activity?.onBackPressed() }
        scipText?.text = RealmHelper.getLocalizedText("auth.skip")
        progressBar?.progress = progress

        LayoutInflater.from(context).inflate(includedLayoutRes, mainContainer, false)?.apply {
            mainContainer?.addView(this)
        }
    }

//    fun updateBackButton(isEnabled: Boolean = false, onClick: (() -> Unit)? = null) {
//        backButton?.isSelected = isEnabled
//        backButton?.setOnClickListener {
//            if (isEnabled) onClick?.invoke() ?: activity?.onBackPressed()
//        }
//    }

    fun updateNextButton(isEnabled: Boolean = false, onClick: () -> Unit) {
        includeNextButton?.isSelected = isEnabled
        includeNextButton?.setOnClickListener { if (isEnabled) onClick() }
    }

    override fun startLoading() {
        super<BaseFragment>.startLoading()
        includeNextButton?.isEnabled = false
        includeNextButton?.nextButtonText?.isVisible = false
        includeNextButton?.nextButtonLottie?.playAnimation()
        includeNextButton?.nextButtonLottie?.isVisible = true
    }

    override fun stopLoading() {
        super<BaseFragment>.stopLoading()
        includeNextButton?.isEnabled = true
        includeNextButton?.nextButtonText?.isVisible = true
        includeNextButton?.nextButtonLottie?.cancelAnimation()
        includeNextButton?.nextButtonLottie?.isVisible = false
    }
}