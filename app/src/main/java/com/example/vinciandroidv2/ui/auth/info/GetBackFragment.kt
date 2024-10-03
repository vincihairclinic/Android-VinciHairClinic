package com.example.vinciandroidv2.ui.auth.info

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.ui.auth.questionnaire.ProcedureOfInterestFragment
import com.example.vinciandroidv2.ui.auth.questionnaire.QuestionnaireFlow
import com.example.vinciandroidv2.ui.auth.questionnaire.questionnaireFlow
import com.example.vinciandroidv2.ui.global.BaseFragment
import com.example.vinciandroidv2.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_email_verification.*

class GetBackFragment : BaseFragment() {
    override val layoutRes = R.layout.fragment_email_verification
    override val TAG = "GetBackFragment"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        image?.setImageResource(R.drawable.ic_check_white_160)
        findButton(R.id.positiveButton)?.apply {
            setText("", R.color.primary_brown_7D5F2B, R.font.montserrat_semi_bold)
            onClick {
                if (questionnaireFlow == QuestionnaireFlow.AUTH) {
                    startActivity(Intent(activity, MainActivity::class.java))
                    activity?.finish()
                } else {
                    activity?.supportFragmentManager?.popBackStack(
                        ProcedureOfInterestFragment().TAG, POP_BACK_STACK_INCLUSIVE
                    )
                }
            }
        }
    }

    override fun setLocalization() {
        super.setLocalization()
        titleField?.text = RealmHelper.getLocalizedText("getback.title.text")
        subTitleField?.text = RealmHelper.getLocalizedText("getback.subtitle.text")
        findButton(R.id.positiveButton)?.setText(RealmHelper.getLocalizedText("getback.button.done.text"))
    }
}