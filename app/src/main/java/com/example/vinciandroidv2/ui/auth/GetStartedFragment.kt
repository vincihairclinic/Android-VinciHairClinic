package com.example.vinciandroidv2.ui.auth

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.di.module.AuthModule
import com.example.vinciandroidv2.di.module.ListModule
import com.example.vinciandroidv2.di.module.UserModule
import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.helper.RealmHelper.copyFromRealm
import com.example.vinciandroidv2.helper.showBaseAlert
import com.example.vinciandroidv2.network.respons.AppState
import com.example.vinciandroidv2.network.respons.Token
import com.example.vinciandroidv2.network.respons.User
import com.example.vinciandroidv2.ui.Screens
import com.example.vinciandroidv2.ui.global.*
import com.example.vinciandroidv2.ui.global.mvp.auth.AuthContract
import com.example.vinciandroidv2.ui.global.mvp.list.ListContract
import com.example.vinciandroidv2.ui.global.mvp.user.UserContract
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.item_row_edit_text_sample_1.view.*
import toothpick.Scope
import toothpick.ktp.delegate.inject

class GetStartedFragment : BaseFragment(), ListContract.View, AuthContract.View, UserContract.View {
    override val layoutRes = R.layout.fragment_sign_up
    override val TAG = "GetStartedFragment"

    private val listPresenter: ListContract.Presenter by inject()
    private val authPresenter: AuthContract.Presenter by inject()
    private val userPresenter: UserContract.Presenter by inject()
    override fun installModules(scope: Scope) {
        scope.installModules(ListModule(this), AuthModule(this), UserModule(this))
    }

    private val userListener = RealmHelper.realm?.where(User::class.java)?.findAll()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        backButton?.setOnClickListener { activity?.onBackPressed() }

        userListener?.addChangeListener { _ -> updateView() }
        includeNameCard?.editTextField?.apply {
            imeOptions = EditorInfo.IME_ACTION_NEXT
            setOnFocusChangeListener { v, hasFocus ->
                (v?.parent as? MaterialCardView)?.isSelected = hasFocus
            }
        }
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

        forgotPasswordButton?.isVisible = false

        findButton(R.id.continueButton)?.apply {
            setText("", R.color.text_white_FFFFFF, R.font.montserrat_semi_bold)
            setBackgroundColor(R.color.primary_brown_7D5F2B)
            onClick {
                hideKeyboard()
                if (isValidForm()) authPresenter.register(getNewUser())
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
            RealmHelper.getLocalizedText("getstarted.terms.text"),
            RealmHelper.getLocalizedText("getstarted.button.termsofuse.text"),
            termsOfServiceLink,
            RealmHelper.getLocalizedText("getstarted.button.privacypolicy.text"),
            privacyPolicyLink,
        )

    }

    fun updateView() {
        RealmHelper.getUserData()?.apply {
            includeNameCard?.editTextField?.setText(this.fullName)
            includeEmailCard?.editTextField?.setText(this.email)
        }
    }

    private fun getNewUser() = User().apply {
        fullName = includeNameCard?.editTextField?.text?.toString() ?: ""
        email = includeEmailCard?.editTextField?.text?.toString() ?: ""
        password = includePasswordCard?.editTextField?.text?.toString() ?: ""
        countryId = selectedCountryId
        languageKey = selectedLanguageKey
    }

    override fun onDestroyView() {
        super.onDestroyView()
        userListener?.removeAllChangeListeners()
    }

    private fun isValidForm() = when {
        (includeNameCard?.editTextField?.text?.length ?: 0) < 2 -> {
            showBaseAlert(
                context,
                title = RealmHelper.getLocalizedText("alerts.moresymbolsneeded.name.text"),
                positiveText = getString(R.string.ok_label)
            )
            false
        }
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
        titleField?.text = RealmHelper.getLocalizedText("getstarted.title.text")
        subTitleField?.text = RealmHelper.getLocalizedText("getstarted.subtitle.text")
        includeNameCard?.editTextField?.hint =
            RealmHelper.getLocalizedText("getstarted.field.name.hint")
        includeEmailCard?.editTextField?.hint =
            RealmHelper.getLocalizedText("getstarted.field.email.hint")
        includePasswordCard?.editTextField?.hint =
            RealmHelper.getLocalizedText("getstarted.field.password.hint")
        findButton(R.id.continueButton)?.setText(RealmHelper.getLocalizedText("getstarted.button.continue.text"))
    }

    override fun userLoggedAuth() {
        RealmHelper.realm?.where(Token::class.java)?.findFirst()?.user?.copyFromRealm()?.let {
            it.appState = AppState.VERIFY_STATE.value
            userPresenter.editUser(it)
        }
    }

    override fun userLoaded() {
        listPresenter.getListsAfterLogin()
    }

    override fun listsAfterLoginLoaded() {
        listPresenter.getLocalization(RealmHelper.getUserData()?.languageKey ?: "en")
    }

    override fun localizationsLoaded() {
        replace(android.R.id.content, Screens.Auth.getEmailVerificationFragment())
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