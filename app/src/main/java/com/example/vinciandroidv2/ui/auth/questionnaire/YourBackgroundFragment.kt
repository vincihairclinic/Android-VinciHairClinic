package com.example.vinciandroidv2.ui.auth.questionnaire

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.style.UnderlineSpan
import androidx.core.text.inSpans
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.helper.RealmHelper.copyFromRealm
import com.example.vinciandroidv2.network.respons.AppState
import com.example.vinciandroidv2.ui.Screens
import kotlinx.android.synthetic.main.fragment_questionnaire_your_background.*
import kotlinx.android.synthetic.main.fragment_sign_up_guestionnaire_base.*
import kotlinx.android.synthetic.main.view_button_sample_one.view.*
import kotlinx.android.synthetic.main.view_next_button.view.*

class YourBackgroundFragment : QuestionnaireBaseFragment() {
    override val includedLayoutRes = R.layout.fragment_questionnaire_your_background
    override val progress = 38
    override val TAG = "YourBackgroundFragment"

    private var familyHairLoss: Boolean? = null
        set(value) {
            field = value
            includeYesButton?.isSelected = true == familyHairLoss
            includeNoButton?.isSelected = false == familyHairLoss
            updateNextButtonView()
        }
    private val sinceHairLossList =
        ArrayList<String>()
            .apply {
                for (i in 0..150) add(
                    if (0 == i) RealmHelper.getLocalizedText("yourbackground.picker.pleaseselect.text")
                    else "$i ${
                        if (1 == i) RealmHelper.getLocalizedText("yourbackground.picker.year.text")
                        else RealmHelper.getLocalizedText("yourbackground.picker.years.text")
                    }"
                )
            }
            .toTypedArray()
    private var selectedItem = 0
        set(value) {
            field = value
            updateNextButtonView()
        }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        scipButton?.setOnClickListener {
            replace(android.R.id.content, Screens.Auth.getProcedureOfInterestFragment())
        }
        hairLossPicker?.apply {
            displayedValues = sinceHairLossList
            minValue = 0
            maxValue = sinceHairLossList.size.minus(1)
            setOnValueChangedListener { _, _, newVal ->
                selectedItem = newVal
            }
        }

        includeYesButton?.setOnClickListener { familyHairLoss = true }
        includeNoButton?.setOnClickListener { familyHairLoss = false }

//        updateBackButton(true) { activity?.onBackPressed() }
    }

    override fun setLocalization() {
        super.setLocalization()
        titleField?.text = RealmHelper.getLocalizedText("yourbackground.title.text")
        backButton?.text = SpannableStringBuilder().inSpans(UnderlineSpan()) {
            append(RealmHelper.getLocalizedText("gender.button.back.text"))
        }
        includeNextButton?.nextButtonText?.text =
            RealmHelper.getLocalizedText("gender.button.next.text")

        hairLossTimeTitle?.text = RealmHelper.getLocalizedText("yourbackground.subtitle.text")
        familyHairLossTitle?.text = RealmHelper.getLocalizedText("yourbackground.subtitle.two.text")
        includeYesButton?.textField?.text =
            RealmHelper.getLocalizedText("yourbackground.button.yes.text")
        includeNoButton?.textField?.text =
            RealmHelper.getLocalizedText("yourbackground.button.no.text")
    }

    private fun updateNextButtonView() {
        updateNextButton(null != familyHairLoss && 0 != selectedItem) {
            RealmHelper.getUserData()?.copyFromRealm()?.let {
                it.howLongHaveYouExperiencedHairLossFor = selectedItem
                it.doesYourFamilySufferFromHereditaryHairLoss = familyHairLoss

                if (questionnaireFlow == QuestionnaireFlow.AUTH) {
                    it.appState = AppState.PROCEDURE_STATE.value
                }

                userPresenter.editUser(it)
            }
        }
    }

    override fun userLoaded() {
        replace(android.R.id.content, Screens.Auth.getProcedureOfInterestFragment())
    }
}