package com.example.vinciandroidv2.ui.auth.questionnaire

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.style.UnderlineSpan
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.core.text.inSpans
import androidx.core.view.isVisible
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.helper.RealmHelper.copyFromRealm
import com.example.vinciandroidv2.network.respons.AppState
import com.example.vinciandroidv2.ui.Screens
import com.example.vinciandroidv2.ui.global.setToStringFormat
import kotlinx.android.synthetic.main.fragment_questionnaire_gender_and_age.*
import kotlinx.android.synthetic.main.fragment_sign_up_guestionnaire_base.*
import kotlinx.android.synthetic.main.view_gender_card.view.*
import kotlinx.android.synthetic.main.view_gender_card.view.drawable
import kotlinx.android.synthetic.main.view_next_button.view.*
import java.util.*

enum class GenderType(val id: Int) { MALE(3), FEMALE(5) }

class GenderAndAgeFragment : QuestionnaireBaseFragment() {
    override val includedLayoutRes = R.layout.fragment_questionnaire_gender_and_age
    override val progress = 19
    override val TAG = "GenderAndAgeFragment"
    private var dateBirthDay = ""
    private var genderType: GenderType? = null
        set(value) {
            field = value
            includeMaleCard?.isSelected = genderType == GenderType.MALE
            includeFemaleCard?.isSelected = genderType == GenderType.FEMALE
            updateNextButton(null != genderType) {
                RealmHelper.getUserData()?.copyFromRealm()?.let {
                    it.genderId = genderType?.id
                    it.dateOfBirth = dateBirthDay
                    if (questionnaireFlow == QuestionnaireFlow.AUTH) {
                        it.appState = AppState.BACKGROUND_STATE.value
                    }
                    userPresenter.editUser(it)
                }
            }
        }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        userPresenter.getUser()

        closeButton?.isVisible = true
        backButton?.isVisible = true
        closeButton?.setOnClickListener {
            activity?.onBackPressed()
        }
        scipButton?.setOnClickListener {
            replace(android.R.id.content, Screens.Auth.getYourBackgroundFragment())
        }
        includeMaleCard?.apply {
            drawable?.setImageResource(R.drawable.ic_male_grey_36)
            setOnClickListener {
                genderType = if (genderType == GenderType.MALE) null else GenderType.MALE
            }
        }
        includeFemaleCard?.apply {
            drawable?.setImageResource(R.drawable.ic_female_grey_36)
            setOnClickListener {
                genderType = if (genderType == GenderType.FEMALE) null else GenderType.FEMALE
            }
        }
        agePicker?.maxDate = System.currentTimeMillis()
        agePicker.setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
            val calendar = Calendar.getInstance()?.apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, monthOfYear)
                set(Calendar.DAY_OF_MONTH, dayOfMonth)
            }

            dateBirthDay = calendar?.time?.setToStringFormat("yyyy-MM-dd").toString()
        }
    }

    override fun setLocalization() {
        super.setLocalization()
        titleField?.text = RealmHelper.getLocalizedText("gender.title.text")
        backButton?.text = SpannableStringBuilder().inSpans(UnderlineSpan()) {
            append(RealmHelper.getLocalizedText("gender.button.back.text"))
        }
        includeNextButton?.nextButtonText?.text =
            RealmHelper.getLocalizedText("gender.button.next.text")

        includeMaleCard?.textField?.text = RealmHelper.getLocalizedText("gender.button.male.text")
        includeFemaleCard?.textField?.text =
            RealmHelper.getLocalizedText("gender.button.female.text")
        backButton?.text = SpannableStringBuilder().inSpans(UnderlineSpan()) {
            append(RealmHelper.getLocalizedText("procedureofinterest.button.back.text"))
        }
//        yearsOldTextField?.text = RealmHelper.getLocalizedText("gender.picker.yrsold.text")
    }

    override fun userLoaded() {
        replace(android.R.id.content, Screens.Auth.getYourBackgroundFragment())
    }
}