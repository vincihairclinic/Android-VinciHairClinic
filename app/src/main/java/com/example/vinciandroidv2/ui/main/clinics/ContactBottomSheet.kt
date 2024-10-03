package com.example.vinciandroidv2.ui.main.clinics

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.helper.openWhatsApp
import com.example.vinciandroidv2.helper.setStartDrawable
import com.example.vinciandroidv2.ui.Screens
import com.example.vinciandroidv2.ui.auth.questionnaire.QuestionnaireFlow
import com.example.vinciandroidv2.ui.auth.questionnaire.questionnaireFlow
import com.example.vinciandroidv2.ui.global.BaseBottomSheetFragment
import com.example.vinciandroidv2.ui.global.requestContactFromClinic
import kotlinx.android.synthetic.main.bottom_sheet_clinic_contact.*
import kotlinx.android.synthetic.main.item_row_drawable_text_view_bottom_divider_sample_1.view.*

class ContactBottomSheet : BaseBottomSheetFragment() {
    override val layoutRes = R.layout.bottom_sheet_clinic_contact
    override val TAG = "ContactBottomSheet"

    var clinicId: Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        includeRequestContact?.apply {
            textField?.apply {
                setStartDrawable(R.drawable.ic_message_brown_24)
                text = RealmHelper.getLocalizedText("clinics.expanded.bottommenu.view.text.one")
            }
            setOnClickListener {
                this@ContactBottomSheet.dismiss()
                questionnaireFlow = QuestionnaireFlow.HOME
                replace(
                    R.id.content,
                    Screens.Auth.getGenderAndAgeFragment()
                )
//                requestContactFromClinic?.let { link ->
//                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
//                }
            }
        }
        includeSendEmail?.apply {
            textField?.apply {
                setStartDrawable(R.drawable.ic_mail_brown_24)
                text = RealmHelper.getLocalizedText("clinics.expanded.bottommenu.view.text.two")
            }
            setOnClickListener {
                this@ContactBottomSheet.dismiss()
                RealmHelper.getClinicById(clinicId ?: return@setOnClickListener)
                    ?.email
                    ?.let { email ->
                        val sendIntent = Intent(Intent.ACTION_SEND)
                        sendIntent.type = "message/rfc822"
                        sendIntent.putExtra(
                            Intent.EXTRA_EMAIL,
                            arrayOf(email)
                        )
                        startActivity(sendIntent)
                    }
            }
        }
        includeWhatsApp?.apply {
            textField?.apply {
                setStartDrawable(R.drawable.ic_whats_app_green_24)
                text = RealmHelper.getLocalizedText("clinics.expanded.bottommenu.view.text.three")
            }
            setOnClickListener {
                this@ContactBottomSheet.dismiss()
                RealmHelper.getClinicById(clinicId ?: return@setOnClickListener)
                    ?.whatsapp
                    ?.let { whatsapp ->
                        activity?.openWhatsApp(whatsapp)
                    }
            }
        }
    }
}