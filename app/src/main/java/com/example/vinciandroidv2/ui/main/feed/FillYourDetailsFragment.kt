package com.example.vinciandroidv2.ui.main.feed

import com.example.vinciandroidv2.ui.global.BaseFragment
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.util.Patterns
import com.bumptech.glide.Glide
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.helper.showBaseAlert
import com.example.vinciandroidv2.network.respons.Country
import com.example.vinciandroidv2.network.respons.Simulation
import com.example.vinciandroidv2.ui.Screens
import com.example.vinciandroidv2.ui.auth.questionnaire.QuestionnaireFlow
import com.example.vinciandroidv2.ui.auth.questionnaire.questionnaireFlow
import com.makeramen.roundedimageview.RoundedImageView
import io.realm.RealmList
import kotlinx.android.synthetic.main.fragment_fill_your_details.*
import kotlinx.android.synthetic.main.fragment_introducing.view.text
import kotlinx.android.synthetic.main.fragment_sign_up.includeEmailCard
import kotlinx.android.synthetic.main.fragment_sign_up.includeNameCard
import kotlinx.android.synthetic.main.fragment_sign_up.includePasswordCard
import kotlinx.android.synthetic.main.fragment_your_number.phoneNumberField
import kotlinx.android.synthetic.main.item_row_edit_text_sample_1.view.*
import kotlinx.android.synthetic.main.view_phone_code_button.view.*
import kotlinx.android.synthetic.main.view_phone_code_button.view.cardContainer

class FillYourDetailsFragment(var simulation: Simulation = Simulation()) : BaseFragment() {
    override val layoutRes: Int
        get() = R.layout.fragment_fill_your_details
    override val TAG: String
        get() = "FillYourDetailsFragment"
    private var selectedItemId = 0
        set(value) {
            field = value
            updateNumCodeView()
        }
    private val list = RealmHelper.realm?.where(Country::class.java)?.findAll() ?: RealmList()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        backButtonText?.setOnClickListener { activity?.onBackPressed() }
        backButton?.setOnClickListener { activity?.onBackPressed() }

        fullName?.editTextField?.addTextChangedListener(object : PhoneNumberFormattingTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrEmpty()) {
                    updateNextButton()
                }
            }
        })
        email?.editTextField?.addTextChangedListener(object : PhoneNumberFormattingTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrEmpty()) {
                    updateNextButton()
                }
            }
        })

        phoneNumber?.editTextField?.hint = "0000 0000"
        codeCountry?.cardContainer?.setBackgroundColor(resources?.getColor(R.color.background_white_FFFFFF))
        codeCountry?.setOnClickListener {
            Screens.Auth.getCountryCodeSelectionBottomSheet { newId ->
                selectedItemId = newId
            }.show(childFragmentManager, "CountrySelectionBottomSheet")
        }
        updateNumCodeView()
    }

    private fun updateNextButton() {
        if (!this@FillYourDetailsFragment.email?.editTextField?.text.isNullOrEmpty() &&
            !this@FillYourDetailsFragment.fullName?.editTextField?.text.isNullOrEmpty() &&
            !this@FillYourDetailsFragment.phoneNumber?.text.isNullOrEmpty()
        ) {
            cardContainerDone.isSelected = true
            cardContainerDone?.setOnClickListener {
                if (isValidForm()){
                    simulation.fullName = fullName?.editTextField?.text?.toString()
                    simulation.email = email?.editTextField?.text?.toString()
                    simulation.phoneNumber = phoneNumber?.text?.toString()
                    replace(android.R.id.content, Screens.Feed.getSimulationRequestFragment(simulation))
                }

            }

        } else {
            cardContainerDone?.isSelected = false
        }
    }

    private fun updateNumCodeView() {
        simulation.countryId = selectedItemId
        (list.firstOrNull { it.id == selectedItemId } ?: list.firstOrNull())?.let {
            Glide.with(this).load(it.urlFlagImage)
                .into(codeCountry?.countryIcon as RoundedImageView)
            codeCountry?.numCodeTextField?.text = it.phoneCode
        }
    }
    private fun isValidForm() = when {
        (email?.editTextField?.text?.length ?: 0) < 2 -> {
            showBaseAlert(
                context,
                title = RealmHelper.getLocalizedText("alerts.moresymbolsneeded.email.text"),
                positiveText = getString(R.string.ok_label)
            )
            false
        }
       !Patterns.EMAIL_ADDRESS.matcher(email?.editTextField?.text ?: "").matches() -> {
            showBaseAlert(
                context,
                title = RealmHelper.getLocalizedText("alerts.moresymbolsneeded.email.text"),
                positiveText = getString(R.string.ok_label)
            )
            false
        }

        else -> true
    }
}

