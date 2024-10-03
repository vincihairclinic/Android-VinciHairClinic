package com.example.vinciandroidv2.ui.auth.questionnaire2

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.core.view.isVisible
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.di.module.UserModule
import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.helper.RealmHelper.copyFromRealm
import com.example.vinciandroidv2.ui.auth.showTermsAndPolicyText
import com.example.vinciandroidv2.ui.global.BaseFragment
import com.example.vinciandroidv2.ui.global.mvp.user.UserContract
import com.example.vinciandroidv2.ui.global.privacyPolicyLink
import com.example.vinciandroidv2.ui.global.sha256
import com.example.vinciandroidv2.ui.global.termsOfServiceLink
import com.example.vinciandroidv2.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_create_password.*
import kotlinx.android.synthetic.main.fragment_create_password.backButton
import kotlinx.android.synthetic.main.fragment_create_password.includeNextButton
import kotlinx.android.synthetic.main.fragment_create_password.progressBar
import kotlinx.android.synthetic.main.fragment_create_password.titleField
import kotlinx.android.synthetic.main.fragment_edit_personal_info.*
import kotlinx.android.synthetic.main.item_row_edit_text_sample_1.view.*
import kotlinx.android.synthetic.main.view_next_button.view.*
import toothpick.Scope
import toothpick.ktp.delegate.inject

class CreatePasswordFragment : BaseFragment(), UserContract.View {
    override val layoutRes: Int
        get() = R.layout.fragment_create_password
    override val TAG: String
        get() = "CreatePasswordFragment"
    private val userPresenter: UserContract.Presenter by inject()
    override fun installModules(scope: Scope) {
        scope.installModules(UserModule(this))
    }

    var isAuth: Boolean = false
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (isAuth) {
            isVisibleButton?.isVisible = true
            termsAndPolicyText?.isVisible = false
            doneInfo?.isVisible = false
        } else {
            isVisibleButton?.isVisible = false
            termsAndPolicyText?.isVisible = true
            doneInfo?.isVisible = true
        }

        progressBar?.progress = 90
        termsAndPolicyText?.showTermsAndPolicyText(
            context,
            RealmHelper.getLocalizedText("getstarted.terms.text"),
            RealmHelper.getLocalizedText("getstarted.button.termsofuse.text"),
            termsOfServiceLink,
            RealmHelper.getLocalizedText("getstarted.button.privacypolicy.text"),
            privacyPolicyLink,
        )
        termsAndPolicyTextAuth?.showTermsAndPolicyText(
            context,
            RealmHelper.getLocalizedText("getstarted.terms.text"),
            RealmHelper.getLocalizedText("getstarted.button.termsofuse.text"),
            termsOfServiceLink,
            RealmHelper.getLocalizedText("getstarted.button.privacypolicy.text"),
            privacyPolicyLink,
        )
        backButton?.setOnClickListener {
            activity?.onBackPressed()
        }

        findButton(R.id.createPasswordButton)?.apply {
            setText(RealmHelper.getLocalizedText("getback.button.done.text"), R.color.background_white_FFFFFF, R.font.montserrat_semi_bold)
            setBackgroundColor(R.color.primary_dark_brown_503629)
            onClick {
                val getTextPassword = this@CreatePasswordFragment.includePasswordCard.editTextField?.getText()?.toString().toString()
                var password: String = ""
                password = getTextPassword.sha256()
                RealmHelper.getUserData()?.copyFromRealm()?.let { u ->
                    u.password = password
                    userPresenter.editUser(u)
                }
            }
        }
        includeNextButton?.nextButtonText?.setOnClickListener {
            val getTextPassword = this@CreatePasswordFragment.includePasswordCard.editTextField?.getText()?.toString().toString()
            var password: String = ""
            password = getTextPassword.sha256()
            RealmHelper.getUserData()?.copyFromRealm()?.let { u ->
                u.password = password
                userPresenter.editUser(u)
            }
        }
        includeNextButton?.nextButtonText?.text =
            RealmHelper.getLocalizedText("getback.button.done.text")
        titleField?.text = RealmHelper.getLocalizedText("getstarted.field.password.hint")
        includePasswordCard.editTextField.hint = RealmHelper.getLocalizedText("getstarted.field.password.hint")
        subTitleField?.text = RealmHelper.getLocalizedText("profile.changepassword.description.text")
        textListener(includePasswordCard.editTextField)
    }

    fun textListener(editText: EditText) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                includeNextButton?.isSelected = !this@CreatePasswordFragment.includePasswordCard.editTextField.text.isNullOrEmpty()

            }
        })
    }

    override fun userLoaded() {
        super.userLoaded()
        if (isAuth){
            activity?.finish()
            startActivity(Intent(activity, MainActivity::class.java))

        }else{
            activity?.finish()
            startActivity(Intent(activity, MainActivity::class.java))
        }

    }
}