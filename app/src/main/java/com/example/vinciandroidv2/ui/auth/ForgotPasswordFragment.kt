package com.example.vinciandroidv2.ui.auth

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.di.module.AuthModule
import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.helper.showBaseAlert
import com.example.vinciandroidv2.ui.global.BaseFragment
import com.example.vinciandroidv2.ui.global.mvp.auth.AuthContract
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.item_row_edit_text_sample_1.view.*
import toothpick.Scope
import toothpick.ktp.delegate.inject

class ForgotPasswordFragment : BaseFragment(), AuthContract.View {
    override val layoutRes = R.layout.fragment_sign_up
    override val TAG = "ForgotPasswordFragment"

    private val authPresenter: AuthContract.Presenter by inject()
    override fun installModules(scope: Scope) {
        scope.installModules(AuthModule(this))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        backButton?.setOnClickListener { activity?.onBackPressed() }

        includeNameCard?.isVisible = false
        includeEmailCard?.editTextField?.apply {
            inputType = EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            setOnFocusChangeListener { v, hasFocus ->
                (v?.parent as? MaterialCardView)?.isSelected = hasFocus
            }
        }
        includePasswordCard?.isVisible = false
        forgotPasswordButton?.isVisible = false

        findButton(R.id.continueButton)?.apply {
            setText("", R.color.text_white_FFFFFF, R.font.montserrat_semi_bold)
            setBackgroundColor(R.color.primary_brown_7D5F2B)
            onClick {
                hideKeyboard()
                if (isValidForm())
                    authPresenter.forgotPassword(
                        this@ForgotPasswordFragment.includeEmailCard?.editTextField?.text?.toString()
                            ?: ""
                    )
            }
        }

        termsAndPolicyText?.isVisible = false
    }

    private fun isValidForm() = when {
        (includeEmailCard?.editTextField?.text?.length ?: 0) < 2 -> {
            showBaseAlert(
                context,
                title = RealmHelper.getLocalizedText("alerts.moresymbolsneeded.email.text"),
                positiveText = getString(R.string.ok_label)
            )
            false
        }
        else -> true
    }

    override fun setLocalization() {
        super.setLocalization()
        titleField?.text = RealmHelper.getLocalizedText("forgotpassword.title.text")
        subTitleField?.text = RealmHelper.getLocalizedText("forgotpassword.subtitle.text")
        includeEmailCard?.editTextField?.hint =
            RealmHelper.getLocalizedText("forgotpassword.field.email.hint")
        findButton(R.id.continueButton)?.setText(RealmHelper.getLocalizedText("forgotpassword.button.resetpassword.text"))
    }

    override fun passwordForgotRequestSent() {
        Toast.makeText(
            context,
            RealmHelper.getLocalizedText("toast.password.resetsent"),
            Toast.LENGTH_LONG
        ).show()
    }

    override fun startLoading() {
        super<BaseFragment>.startLoading()
        findButton(R.id.continueButton)?.startLoading()
    }

    override fun stopLoading() {
        super<BaseFragment>.stopLoading()
        findButton(R.id.continueButton)?.stopLoading()
    }
}