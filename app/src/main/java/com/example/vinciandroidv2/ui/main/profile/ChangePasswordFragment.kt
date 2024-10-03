package com.example.vinciandroidv2.ui.main.profile

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.style.UnderlineSpan
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.text.inSpans
import androidx.core.view.isVisible
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.di.module.AuthModule
import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.helper.showBaseAlert
import com.example.vinciandroidv2.ui.global.BaseFragment
import com.example.vinciandroidv2.ui.global.isPasswordValid
import com.example.vinciandroidv2.ui.global.mvp.auth.AuthContract
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.fragment_change_password.*
import kotlinx.android.synthetic.main.item_row_edit_text_sample_1.view.*
import toothpick.Scope
import toothpick.ktp.delegate.inject

class ChangePasswordFragment : BaseFragment(), AuthContract.View {
    override val layoutRes = R.layout.fragment_change_password
    override val TAG = "ChangePasswordFragment"

    private val authPresenter: AuthContract.Presenter by inject()
    override fun installModules(scope: Scope) {
        scope.installModules(AuthModule(this))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        backButton?.setOnClickListener { activity?.onBackPressed() }
        saveButton?.apply {
            isSelected = true
            setOnClickListener {
                hideKeyboard()
                if (isValidForm()) {
                    authPresenter.changePassword(
                        this@ChangePasswordFragment.includeCurrentPasswordCard?.editTextField?.text?.toString()
                            ?: "",
                        this@ChangePasswordFragment.includeNewPasswordCard?.editTextField?.text?.toString()
                            ?: "",
                    )
                }
            }
        }
        includeCurrentPasswordCard?.editTextField?.apply {
            imeOptions = EditorInfo.IME_ACTION_NEXT
            inputType = EditorInfo.TYPE_TEXT_VARIATION_PASSWORD
            setOnFocusChangeListener { v, hasFocus ->
                (v?.parent as? MaterialCardView)?.isSelected = hasFocus
            }
        }
        includeNewPasswordCard?.editTextField?.apply {
            imeOptions = EditorInfo.IME_ACTION_NEXT
            inputType = EditorInfo.TYPE_TEXT_VARIATION_PASSWORD
            setOnFocusChangeListener { v, hasFocus ->
                (v?.parent as? MaterialCardView)?.isSelected = hasFocus
            }
        }
        includeConfirmPasswordCard?.editTextField?.apply {
            inputType = EditorInfo.TYPE_TEXT_VARIATION_PASSWORD
            setOnFocusChangeListener { v, hasFocus ->
                (v?.parent as? MaterialCardView)?.isSelected = hasFocus
            }
        }
    }

    override fun setLocalization() {
        super.setLocalization()
        saveButton?.text = SpannableStringBuilder().inSpans(UnderlineSpan()) {
            append(RealmHelper.getLocalizedText("profile.changepassword.button.save.text"))
        }
        titleField?.text = RealmHelper.getLocalizedText("profile.changepassword.title.text")
        passwordValidatorInformationTextField?.text =
            RealmHelper.getLocalizedText("profile.changepassword.description.text")
        includeCurrentPasswordCard?.editTextField?.hint =
            RealmHelper.getLocalizedText("profile.changepassword.field.current.hint")
        includeNewPasswordCard?.editTextField?.hint =
            RealmHelper.getLocalizedText("profile.changepassword.field.new.hint")
        includeConfirmPasswordCard?.editTextField?.hint =
            RealmHelper.getLocalizedText("profile.changepassword.field.confirm.hint")
    }

    private fun isValidForm() = when {
        (includeCurrentPasswordCard?.editTextField?.text?.toString()?.length ?: 0) < 2 -> {
            showBaseAlert(
                context,
                title = RealmHelper.getLocalizedText("alerts.notvalid.currentpassword.text"),
                positiveText = getString(R.string.ok_label)
            )
            false
        }
        includeNewPasswordCard?.editTextField?.text?.toString()?.isPasswordValid() == false -> {
            showBaseAlert(
                context,
                title = RealmHelper.getLocalizedText("alerts.notvalid.newpassword.text"),
                positiveText = getString(R.string.ok_label)
            )
            false
        }
        includeNewPasswordCard?.editTextField?.text?.toString() !=
                includeConfirmPasswordCard?.editTextField?.text?.toString() -> {
            showBaseAlert(
                context,
                title = RealmHelper.getLocalizedText("alerts.notvalid.newandconfirmpasswords.notthesame"),
                positiveText = getString(R.string.ok_label)
            )
            false
        }
        else -> true
    }

    override fun passwordChanged() {
        Toast.makeText(
            context,
            RealmHelper.getLocalizedText("toast.paswword.changed"),
            Toast.LENGTH_LONG
        ).show()
        activity?.onBackPressed()
    }

    override fun startLoading() {
        super<BaseFragment>.startLoading()
        saveButton?.isVisible = false
        saveLotti?.playAnimation()
        saveLotti?.isVisible = true
    }

    override fun stopLoading() {
        super<BaseFragment>.stopLoading()
        saveButton?.isVisible = true
        saveLotti?.isVisible = false
        saveLotti?.cancelAnimation()
    }
}