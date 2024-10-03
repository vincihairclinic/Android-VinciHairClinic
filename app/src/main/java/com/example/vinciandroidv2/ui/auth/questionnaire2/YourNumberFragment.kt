package com.example.vinciandroidv2.ui.auth.questionnaire2

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.widget.EditText
import androidx.core.text.inSpans
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.di.module.UserModule
import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.helper.RealmHelper.copyFromRealm
import com.example.vinciandroidv2.network.respons.AppState
import com.example.vinciandroidv2.network.respons.Country
import com.example.vinciandroidv2.ui.Screens
import com.example.vinciandroidv2.ui.auth.questionnaire.QuestionnaireFlow
import com.example.vinciandroidv2.ui.auth.questionnaire.questionnaireFlow
import com.example.vinciandroidv2.ui.global.BaseFragment
import com.example.vinciandroidv2.ui.global.mvp.user.UserContract
import com.example.vinciandroidv2.ui.global.selectedCountryId
import com.makeramen.roundedimageview.RoundedImageView
import io.realm.RealmList
import kotlinx.android.synthetic.main.fragment_your_number.*
import kotlinx.android.synthetic.main.fragment_your_number.backButton
import kotlinx.android.synthetic.main.fragment_your_number.includeNextButton
import kotlinx.android.synthetic.main.fragment_your_number.titleField
import kotlinx.android.synthetic.main.item_row_edit_text_sample_1.view.*
import kotlinx.android.synthetic.main.view_next_button.view.*
import kotlinx.android.synthetic.main.view_phone_code_button.view.*
import toothpick.Scope
import toothpick.ktp.delegate.inject

class YourNumberFragment : BaseFragment(), UserContract.View {
    override val layoutRes = R.layout.fragment_your_number
    override val TAG = "YourNumberFragment"

    private val userPresenter: UserContract.Presenter by inject()
    override fun installModules(scope: Scope) {
        scope.installModules(UserModule(this))
    }
    var isBook = false
    private val list = RealmHelper.realm?.where(Country::class.java)?.findAll() ?: RealmList()
    private var selectedItemId = 0
        set(value) {
            field = value
            updateNumCodeView()
        }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        backButtonText?.setOnClickListener {
            activity?.onBackPressed()
        }

        this@YourNumberFragment.selectedItemId = selectedCountryId
        if (isBook){
            scipButton?.isVisible = true
            scipText?.isVisible = true
            scipButton?.setOnClickListener {
                replace(android.R.id.content, Screens.Auth.getPreferredClinicFragment())
            }
        }

        backButton?.setOnClickListener { activity?.onBackPressed() }
        includePhonecodeCard?.setOnClickListener {
            Screens.Auth.getCountryCodeSelectionBottomSheet { newId ->
                selectedItemId = newId
            }.show(childFragmentManager, "CountrySelectionBottomSheet")
        }
        updateNumCodeView()

        phoneNumberField?.addTextChangedListener(object : PhoneNumberFormattingTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                val phoneNumber = StringBuilder()

                phoneNumberField?.text?.toString()?.toCharArray()
                    ?.forEach {
                        if (phoneNumber.length < 10 && it.isDigit()) {
                            phoneNumber.append(it)
                            updateNextButton()
                        }
                    }

                if (phoneNumber.isEmpty()) phoneNumberField?.text?.clear()
//                else phoneNumber.insert(0, "+")

                if (phoneNumber.length > 3) phoneNumber.insert(3, " ")
                if (phoneNumber.length > 7) phoneNumber.insert(7, " ")
//                if (phoneNumber.length > 12) phoneNumber.insert(12, " ")

                phoneNumberField?.removeTextChangedListener(this)
                phoneNumberField?.setText(phoneNumber)
                phoneNumberField?.setSelection(phoneNumberField?.text?.toString()?.length ?: 0)
                phoneNumberField?.addTextChangedListener(this)
            }
        })
        includeNameCard?.editTextField?.addTextChangedListener(object : PhoneNumberFormattingTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrEmpty()) {
                    updateNextButton()
                }
            }
        })
        includeEmailCard?.editTextField?.addTextChangedListener(object : PhoneNumberFormattingTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrEmpty()) {
                    updateNextButton()
                }
            }
        })
        if (QuestionnaireFlow.HOME == questionnaireFlow) {
            RealmHelper.getUserData()?.phoneNumber?.let { number ->
                val code = number.removeRange(number.length.minus(10), number.length)
                list.firstOrNull { it.phoneCode == code }?.id
                selectedItemId = list.firstOrNull { it.phoneCode == code }?.id ?: 0
                phoneNumberField?.setText(number.takeLast(10))
            }
        }
        this@YourNumberFragment.includeEmailCard?.editTextField?.let { textListener(it) }
        this@YourNumberFragment.includeNameCard?.editTextField?.let { textListener(it) }
    }

    private fun updateNumCodeView() {
        (list.firstOrNull { it.id == selectedItemId } ?: list.firstOrNull())?.let {
            Glide.with(this).load(it.urlFlagImage)
                .into(includePhonecodeCard?.countryIcon as RoundedImageView)
            includePhonecodeCard?.numCodeTextField?.text = it.phoneCode
        }
    }

    private fun updateNextButton() {
        if (!this@YourNumberFragment.includeEmailCard?.editTextField?.text.isNullOrEmpty() &&
            !this@YourNumberFragment.includeNameCard?.editTextField?.text.isNullOrEmpty() &&
            !this@YourNumberFragment.phoneNumberField?.text.isNullOrEmpty()) {
            includeNextButton?.isSelected = true
            includeNextButton?.isEnabled = true
            includeNextButton?.setOnClickListener {
                RealmHelper.getUserData()?.copyFromRealm()?.let {
                    val phoneNum = phoneNumberField?.text?.toString()?.replace(" ", "") ?: ""
                    val code =
                        list.firstOrNull { item -> item.id == selectedItemId }?.phoneCode ?: ""
                    it.phoneNumber = "${code}${phoneNum}"
                    it.fullName = this@YourNumberFragment.includeNameCard?.editTextField?.text?.toString()
                    it.email = this@YourNumberFragment.includeEmailCard?.editTextField?.text?.toString()
                    if (questionnaireFlow == QuestionnaireFlow.AUTH) {
                        it.appState = AppState.HAIR_PHOTOS_STATE.value
                    }

                    userPresenter.editUser(it)
                }
            }
        } else {
//            includeNextButton?.isSelected = false
            includeNextButton?.setOnClickListener(null)
        }
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
                updateNextButton()
            }
        })
    }
    override fun setLocalization() {
        super.setLocalization()
        titleField?.text = RealmHelper.getLocalizedText("phone.title.text")
        includeNextButton?.nextButtonText?.text =
            RealmHelper.getLocalizedText("hairphotos.button.next.text")
        includeNameCard?.editTextField?.hint =
            RealmHelper.getLocalizedText("getstarted.field.name.hint")
        includeEmailCard?.editTextField?.hint =
            RealmHelper.getLocalizedText("getstarted.field.email.hint")
        backButtonText?.text = SpannableStringBuilder().inSpans(android.text.style.UnderlineSpan()) {
            append(com.example.vinciandroidv2.helper.RealmHelper.getLocalizedText("procedureofinterest.button.back.text"))
        }
    }

    override fun userLoaded() {
        this@YourNumberFragment.includeNameCard?.editTextField?.setText(RealmHelper?.getUserData()?.fullName)
        add(android.R.id.content, Screens.Auth.getPreferredClinicFragment())
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