package com.example.vinciandroidv2.ui.auth.onboarding

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import androidx.core.text.inSpans
import com.bumptech.glide.Glide
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.ui.global.BaseFragment
import kotlinx.android.synthetic.main.fragment_on_boarding_step.*

class OnBoardingStepFragment(private val step: Int) : BaseFragment() {
    override val layoutRes = R.layout.fragment_on_boarding_step
    override val TAG = "OnBoardingStepFragment"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Glide.with(this).load(
            when (step) {
                1 -> R.drawable.photo_on_boarding_step_1
                2 -> R.drawable.photo_on_boarding_step_2
                3 -> R.drawable.photo_on_boarding_step_3
                else -> return
            }
        ).into(stepImage)

        arrayOf(step1ColorView, step2ColorView, step3ColorView).forEachIndexed { index, view ->
            if (step == index.plus(1)) {
                view?.setBackgroundColor(resources.getColor(R.color.primary_brown_7D5F2B))
            } else view?.setBackgroundColor(resources.getColor(R.color.background_light_brown_F1F0EA))
//
//            view?.setBackgroundColor(resources.getColor(R.color.selector_brown_light_brown))
//            view?.isSelected = step == index.plus(1)
        }
    }

    override fun setLocalization() {
        super.setLocalization()
        when (step) {
            1 -> {
                stepTitleField?.text = SpannableStringBuilder()
                    .append(RealmHelper.getLocalizedText("onboarding.step.one.title.text.one"))
                    .append(" ")
                    .inSpans(ForegroundColorSpan(Color.parseColor("#7D5F2B"))) {
                        append(RealmHelper.getLocalizedText("onboarding.step.one.title.text.two"))
                    }
            }
            2 -> {
                stepTitleField?.text = SpannableStringBuilder()
                .append(RealmHelper.getLocalizedText("onboarding.step.two.title.text.one"))
                .append(" ")
                .inSpans(ForegroundColorSpan(Color.parseColor("#7D5F2B"))) {
                    append(RealmHelper.getLocalizedText("onboarding.step.two.title.text.two"))
                }
            }
            3 -> {
                stepTitleField?.text = SpannableStringBuilder()
                    .append(RealmHelper.getLocalizedText("onboarding.step.three.title.text.one"))
                    .append(" ")
                    .inSpans(ForegroundColorSpan(Color.parseColor("#7D5F2B"))) {
                        append(RealmHelper.getLocalizedText("onboarding.step.three.title.text.two"))
                    }
            }
            else -> return
        }

    }
}