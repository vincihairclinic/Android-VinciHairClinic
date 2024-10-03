package com.example.vinciandroidv2.ui.auth

import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.style.UnderlineSpan
import android.view.inputmethod.EditorInfo
import androidx.core.text.inSpans
import androidx.core.view.isVisible
import com.example.vinciandroidv2.App
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.di.module.AuthModule
import com.example.vinciandroidv2.di.module.ListModule
import com.example.vinciandroidv2.di.module.UserModule
import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.helper.showBaseAlert
import com.example.vinciandroidv2.network.respons.AppState
import com.example.vinciandroidv2.ui.Screens
import com.example.vinciandroidv2.ui.global.BaseFragment
import com.example.vinciandroidv2.ui.global.mvp.auth.AuthContract
import com.example.vinciandroidv2.ui.global.mvp.list.ListContract
import com.example.vinciandroidv2.ui.global.mvp.user.UserContract
import com.example.vinciandroidv2.ui.global.privacyPolicyLink
import com.example.vinciandroidv2.ui.global.termsOfServiceLink
import com.example.vinciandroidv2.ui.main.MainActivity
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.item_row_edit_text_sample_1.view.*
import toothpick.Scope
import toothpick.ktp.delegate.inject

class SignInFragment : BaseFragment(), ListContract.View, AuthContract.View, UserContract.View {
    override val layoutRes = R.layout.fragment_sign_up
    override val TAG = "SignInFragment"

    private val listPresenter: ListContract.Presenter by inject()
    private val authPresenter: AuthContract.Presenter by inject()
    private val userPresenter: UserContract.Presenter by inject()
    override fun installModules(scope: Scope) {
        scope.installModules(ListModule(this), AuthModule(this), UserModule(this))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        backButton?.setOnClickListener { activity?.onBackPressed() }

        includeNameCard?.isVisible = false
        includeEmailCard?.editTextField?.apply {
            imeOptions = EditorInfo.IME_ACTION_NEXT
            inputType = EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            setOnFocusChangeListener { v, hasFocus ->
                (v?.parent as? MaterialCardView)?.isSelected = hasFocus
            }
        }
        includePasswordCard?.editTextField?.apply {
            inputType = EditorInfo.TYPE_TEXT_VARIATION_PASSWORD
            setOnFocusChangeListener { v, hasFocus ->
                (v?.parent as? MaterialCardView)?.isSelected = hasFocus
            }
        }

        forgotPasswordButton?.setOnClickListener {
            replace(R.id.content, Screens.Auth.getForgotPasswordFragment())
        }

        findButton(R.id.continueButton)?.apply {
            setText("", R.color.text_white_FFFFFF, R.font.montserrat_semi_bold)
            setBackgroundColor(R.color.primary_brown_7D5F2B)
            onClick {
                hideKeyboard()
                if (isValidForm())
                    authPresenter.login(
                        this@SignInFragment.includeEmailCard?.editTextField?.text?.toString()
                            ?: "",
                        this@SignInFragment.includePasswordCard?.editTextField?.text?.toString()
                            ?: ""
                    )
            }
        }

//        termsAndPolicyText?.apply {
//            text = SpannableStringBuilder()
//                .append("By signing up, you agree to our ")
//                .inSpans(
//                    getFontStyle(context, R.font.montserrat_semi_bold),
//                    URLSpan(termsOfServiceLink),
//                    UnderlineSpan(),
//                    ForegroundColorSpan(Color.parseColor("#7D5F2B"))
//                ) { append("Terms of Use") }
//                .append(" and acknowledge that our ")
//                .inSpans(
//                    getFontStyle(context, R.font.montserrat_semi_bold),
//                    URLSpan(privacyPolicyLink),
//                    UnderlineSpan(),
//                    ForegroundColorSpan(Color.parseColor("#7D5F2B")),
//                ) { append("Privacy Policy") }
//                .append(" applies to you.")
//            linksClickable = true
//            movementMethod = LinkMovementMethod.getInstance()
//        }
        termsAndPolicyText?.showTermsAndPolicyText(
            context,
            RealmHelper.getLocalizedText("signin.terms.text"),
            RealmHelper.getLocalizedText("signin.button.termsofuse.text"),
            termsOfServiceLink,
            RealmHelper.getLocalizedText("signin.button.privacypolicy.text"),
            privacyPolicyLink,
        )
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
        (includePasswordCard?.editTextField?.text?.length ?: 0) < 2 -> {
            showBaseAlert(
                context,
                title = RealmHelper.getLocalizedText("alerts.moresymbolsneeded.password.text"),
                positiveText = getString(R.string.ok_label)
            )
            false
        }
        else -> true
    }

    override fun setLocalization() {
        super.setLocalization()
        titleField?.text = RealmHelper.getLocalizedText("signin.title.text")
        subTitleField?.text = RealmHelper.getLocalizedText("signin.subtitle.text")
        includeEmailCard?.editTextField?.hint =
            RealmHelper.getLocalizedText("signin.field.email.hint")
        includePasswordCard?.editTextField?.hint =
            RealmHelper.getLocalizedText("signin.field.password.hint")
        forgotPasswordButton?.text =
            SpannableStringBuilder().inSpans(UnderlineSpan()) {
                append(RealmHelper.getLocalizedText("signin.button.forgotpassword.text"))
            }
        findButton(R.id.continueButton)?.setText(RealmHelper.getLocalizedText("signin.button.continue.text"))
    }

    override fun userLoggedAuth() {
        userPresenter.getUser()
    }

    override fun userLoaded() {
        RealmHelper.getUserData()?.countryId?.let { countryId ->
            com.example.vinciandroidv2.ui.global.host =
                RealmHelper.getHostFromSelectedCountry(countryId)
            (activity?.application as App).updateAppScope()
        }
        listPresenter.getListsAfterLogin()
    }

    override fun listsAfterLoginLoaded() {
        listPresenter.getLocalization(RealmHelper.getUserData()?.languageKey ?: "en")
    }

    override fun localizationsLoaded() {
        val userState = RealmHelper.getUserData()?.appState

        if (userState == AppState.COMPLETED_STATE.value) {
            startActivity(Intent(activity, MainActivity::class.java))
            activity?.finish()
        } else {
            replace(
                android.R.id.content,
                (activity as AuthActivity).getDestinationOfCurrentAppState(userState)
            )
        }
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