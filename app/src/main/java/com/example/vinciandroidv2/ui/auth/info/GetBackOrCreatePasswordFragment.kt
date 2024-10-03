package com.example.vinciandroidv2.ui.auth.info

import android.content.Intent
import android.os.Bundle
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.ui.Screens
import com.example.vinciandroidv2.ui.global.BaseFragment
import com.example.vinciandroidv2.ui.global.dp
import com.example.vinciandroidv2.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_get_back_or_create_password.*

class GetBackOrCreatePasswordFragment : BaseFragment() {
    override val layoutRes: Int
        get() = R.layout.fragment_get_back_or_create_password
    override val TAG: String
        get() = "GetBackOrCreatePasswordFragment"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        image?.setImageResource(R.drawable.ic_check_white_160)
        findButton(R.id.skipButton)?.apply {
            setText(RealmHelper.getLocalizedText("auth.skip"), R.color.primary_dark_brown_503629, R.font.montserrat_semi_bold)
            setBackgroundColor(R.color.background_white_FFFFFF)
            setBorder(R.color.primary_dark_brown_503629, 1.dp)
            onClick {
                activity?.finish()
                startActivity(Intent(activity, MainActivity::class.java))
            }
        }

        findButton(R.id.setUpButton)?.apply {
            setText(RealmHelper.getLocalizedText("button.setup"), R.color.text_white_FFFFFF, R.font.montserrat_semi_bold)
            setBackgroundColor(R.color.primary_brown_7D5F2B)
            onClick {
                replace(android.R.id.content, Screens.Auth.getCreatePasswordFragment().apply {
                    isAuth = true
                })
            }
        }
    }

    override fun setLocalization() {
        titleField?.text = RealmHelper.getLocalizedText("getback.title.text")
        subTitleField?.text = RealmHelper.getLocalizedText("getback.subtitle.text")
    }
}