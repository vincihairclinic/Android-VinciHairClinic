package com.example.vinciandroidv2.ui.main.profile

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.style.UnderlineSpan
import androidx.core.text.inSpans
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.ui.Screens
import com.example.vinciandroidv2.ui.global.BaseFragment
import kotlinx.android.synthetic.main.bottom_sheet_country_selection.*
import kotlinx.android.synthetic.main.fragment_introducing.*

class IntroducingFragment : BaseFragment() {
    override val layoutRes: Int
        get() = R.layout.fragment_introducing
    override val TAG: String
        get() = "IntroducingFragment"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        cancelButton?.apply {
//            isSelected = true
//            text = SpannableStringBuilder().inSpans(UnderlineSpan()) {
//                append(RealmHelper.getLocalizedText("gender.button.back.text"))
//            }
//            setOnClickListener { }
//        }
//        findButton(R.id.saveButton)?.apply {
//            setText(
//                RealmHelper.getLocalizedText("Request Simulation"),
//                R.color.text_white_FFFFFF,
//                R.font.montserrat_semi_bold
//            )
//            setBackgroundColor(R.color.primary_brown_7D5F2B)
//            onClick {
//                replace(R.id.content, Screens.Feed.getSelectTypeOfSimulationFragment())
//            }
//        }
        backButtonText?.setOnClickListener {
            activity?.onBackPressed()
        }
        closeButton?.setOnClickListener {
            activity?.onBackPressed()
        }
        cardContainer?.isSelected = true
        nextButtonText?.setOnClickListener {
            replace(R.id.content, Screens.Feed.getSelectTypeOfSimulationFragment())
        }
    }
}