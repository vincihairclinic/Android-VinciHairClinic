package com.example.vinciandroidv2.ui.main.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.example.vinciandroidv2.App
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.helper.RealmHelper.copyFromRealm
import com.example.vinciandroidv2.helper.setEndDrawable
import com.example.vinciandroidv2.helper.setStartDrawable
import com.example.vinciandroidv2.helper.setStartEndDrawable
import com.example.vinciandroidv2.ui.Screens
import com.example.vinciandroidv2.ui.SplashActivity
import com.example.vinciandroidv2.ui.auth.questionnaire.QuestionnaireFlow
import com.example.vinciandroidv2.ui.auth.questionnaire.questionnaireFlow
import com.example.vinciandroidv2.ui.global.BaseBottomSheetFragment
import com.example.vinciandroidv2.ui.global.privacyPolicyLink
import com.example.vinciandroidv2.ui.global.requestContactFromClinic
import com.example.vinciandroidv2.ui.global.termsOfServiceLink
import com.example.vinciandroidv2.ui.main.MainActivity
import kotlinx.android.synthetic.main.bottom_sheet_profile.*
import kotlinx.android.synthetic.main.item_row_drawable_text_view_bottom_divider_sample_1.view.*

class ProfileBottomSheet : BaseBottomSheetFragment() {
    override val layoutRes = R.layout.bottom_sheet_profile
    override val TAG = "ProfileBottomSheet"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        includePersonalInformationView?.apply {
            textField?.apply {
                RealmHelper.getUserData()?.copyFromRealm().let {
                    if (it?.fullName.isNullOrEmpty() && it?.email.isNullOrEmpty()) {
                        text = RealmHelper.getLocalizedText("profile.bottomsheet.view.text.setup.account")
                        setBackgroundResource(R.drawable.ba_border_line_brown)
                        setPadding(20, 0, 20, 0)
                        setStartEndDrawable(R.drawable.ic_profile_round_brown_24, R.drawable.ic_arrow_right_brown_16)
                    } else {
                        setStartDrawable(R.drawable.ic_profile_round_brown_24)
                        text = RealmHelper.getLocalizedText("profile.bottomsheet.view.text.one")
                    }
                }
            }
            divider?.isVisible = false
            setOnClickListener {
                this@ProfileBottomSheet.dismiss()
                replace(R.id.content, Screens.Profile.getEditPersonalInfoFragment())
            }
        }
        includeHairLossJourneyView?.apply {
            divider?.isVisible = true
            textField?.apply {
                setStartDrawable(R.drawable.ic_pencil_round_brown_24)
                text = RealmHelper.getLocalizedText("profile.bottomsheet.view.text.two")
            }
            setOnClickListener {
                this@ProfileBottomSheet.dismiss()
                questionnaireFlow = QuestionnaireFlow.PROFILE
                replace(R.id.content, Screens.Auth.getProcedureOfInterestFragment())
            }
        }
        myHairLossJourneyView?.apply {
            divider?.isVisible = true
            textField?.apply {
                setStartDrawable(R.drawable.ic_pencil_round_brown_24)

                text = RealmHelper.getLocalizedText("profile.bottomsheet.view.text.two")
                text = "My Hair Loss Journey"
            }
            setOnClickListener {
                this@ProfileBottomSheet.dismiss()
                questionnaireFlow = QuestionnaireFlow.PROFILE
                replace(R.id.content, Screens.Feed.getIntroducingFragment())
            }
        }
        includeRegionalSettingsView?.apply {
            textField?.apply {
                setStartDrawable(R.drawable.ic_globe_round_brown_24)
                text = RealmHelper.getLocalizedText("profile.bottomsheet.view.text.three")
            }
            setOnClickListener {
                this@ProfileBottomSheet.dismiss()
                replace(R.id.content, Screens.Profile.getRegionalSettingsFragment())
            }
        }
        includeNotificationsView?.apply {
            textField?.apply {
                setStartDrawable(R.drawable.ic_notifications_round_brown_24)
                text = RealmHelper.getLocalizedText("profile.bottomsheet.view.text.four")
            }
            setOnClickListener {
                this@ProfileBottomSheet.dismiss()
                replace(R.id.content, Screens.Profile.getNotificationsFragment())
            }
        }
        includeGetHelpView?.apply {
            textField?.apply {
                setStartDrawable(R.drawable.ic_question_mark_round_brown_24)
                text = RealmHelper.getLocalizedText("profile.bottomsheet.view.text.five")
            }
            setOnClickListener {
                requestContactFromClinic?.let { link ->
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
                }
            }
        }
        includeGiveFeedbackView?.apply {
            textField?.apply {
                setStartDrawable(R.drawable.ic_pencil_round_brown_24)
                text = RealmHelper.getLocalizedText("profile.bottomsheet.view.text.six")
            }
            setOnClickListener {
                requestContactFromClinic?.let { link ->
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
                }
            }
        }
        includeTermsOfServiceView?.apply {
            textField?.apply {
                setStartDrawable(R.drawable.ic_document_round_brown_24)
                text = RealmHelper.getLocalizedText("profile.bottomsheet.view.text.seven")
            }
            setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(termsOfServiceLink)))
            }
        }
        includePrivacyPolicyView?.apply {
            textField?.apply {
                setStartDrawable(R.drawable.ic_document_round_brown_24)
                text = RealmHelper.getLocalizedText("profile.bottomsheet.view.text.eight")
            }
            setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(privacyPolicyLink)))
            }
        }
        includeLogOutView?.apply {
            textField?.apply {
                setStartDrawable(R.drawable.ic_log_out_round_brown_24)
                text = RealmHelper.getLocalizedText("profile.bottomsheet.view.text.nine")
            }
            setOnClickListener {
//                RealmHelper.removeSensitiveDataFromBase()
//                (activity?.application as App).updateAppScope()

//                startActivity(Intent(context, SplashActivity::class.java))
//                activity?.finish()

                (activity as? MainActivity)?.logOutUser()
            }
        }
    }
}