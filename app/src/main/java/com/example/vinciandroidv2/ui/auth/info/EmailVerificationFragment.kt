package com.example.vinciandroidv2.ui.auth.info

import android.os.Bundle
import android.os.Handler
import android.text.SpannableStringBuilder
import androidx.core.text.inSpans
import androidx.core.view.isVisible
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.di.module.UserModule
import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.helper.RealmHelper.copyFromRealm
import com.example.vinciandroidv2.network.respons.AppState
import com.example.vinciandroidv2.network.respons.User
import com.example.vinciandroidv2.ui.Screens
import com.example.vinciandroidv2.ui.global.BaseFragment
import com.example.vinciandroidv2.ui.global.getFontStyle
import com.example.vinciandroidv2.ui.global.mvp.user.UserContract
import kotlinx.android.synthetic.main.fragment_email_verification.*
import toothpick.Scope
import toothpick.ktp.delegate.inject

class EmailVerificationFragment : BaseFragment(), UserContract.View {
    override val layoutRes = R.layout.fragment_email_verification
    override val TAG = "EmailVerificationFragment"

    private val userPresenter: UserContract.Presenter by inject()
    override fun installModules(scope: Scope) {
        scope.installModules(UserModule(this))
    }

    private val userListener = RealmHelper.realm?.where(User::class.java)?.findAll()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        image?.setImageResource(R.drawable.ic_mail_white_160)
        userListener?.addChangeListener { _ -> updateViews() }
        updateViews()

        userPresenter.getUser()

//        /** temp button click **/
//        image?.setOnClickListener {
//            image?.setImageResource(R.drawable.ic_check_white_160)
//            titleField?.text = "Thank you!"
//            subTitleField?.text =
//                "We want to give you a personalised experience, please answer a few questions about your hair loss history so that we show you content relevant to your unique case"
//            positiveButton?.isVisible = true
//            positiveButton?.setOnClickListener {
//                RealmHelper.getUserData()?.let { u ->
//                    val user = realm.copyFromRealm(u)
//                    user.appState = AppState.GENDER_STATE.value
//                    userPresenter.editUser(user)
//                }
//            }
//        }
    }

    override fun setLocalization() {
        super.setLocalization()
        titleField?.text = RealmHelper.getLocalizedText("emailverification.checkinbox.title.text")
        subTitleField?.text = SpannableStringBuilder()
            .append(RealmHelper.getLocalizedText("emailverification.checkinbox.subtitle.text"))
            .append(" ")
            .inSpans(
                getFontStyle(context, R.font.montserrat_semi_bold),
            ) { append(RealmHelper.getUserData()?.email ?: "") }
    }

    private fun updateViews() {
        (RealmHelper.getUserData()?.isEmailVerified ?: false).let { isVerified ->
            if (isVerified) {
                image?.setImageResource(R.drawable.ic_check_white_160)
                titleField?.text =
                    RealmHelper.getLocalizedText("emailverification.verified.title.text")
                subTitleField?.text =
                    RealmHelper.getLocalizedText("emailverification.verified.subtitle.text")
                findButton(R.id.positiveButton)?.setText(
                    RealmHelper.getLocalizedText("emailverification.button.continue.text"),
                    R.color.primary_brown_7D5F2B,
                    R.font.montserrat_semi_bold
                )
                positiveButton?.isVisible = true
                positiveButton?.setOnClickListener {
                    RealmHelper.getUserData()?.copyFromRealm()?.let { u ->
                        u.appState = AppState.GENDER_STATE.value
                        userPresenter.editUser(u)
                    }
                }
            } else {
                image?.setImageResource(R.drawable.ic_mail_white_160)
                positiveButton?.isVisible = false
                positiveButton?.setOnClickListener(null)
            }
        }
    }

    override fun userLoaded() {
        RealmHelper.getUserData()?.let { user ->
            if (user.isEmailVerified) {
                if (user.appState == AppState.GENDER_STATE.value) {
                    replace(android.R.id.content, Screens.Auth.getGenderAndAgeFragment())
                } else {

                }
            } else {
                Handler().postDelayed({
                    userPresenter.getUser()
                }, 20000)
            }
        }
    }

    override fun startLoading() {
        super<BaseFragment>.startLoading()
        findButton(R.id.positiveButton)?.startLoading()
    }

    override fun stopLoading() {
        super<BaseFragment>.stopLoading()
        findButton(R.id.positiveButton)?.stopLoading()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        userListener?.removeAllChangeListeners()
    }
}