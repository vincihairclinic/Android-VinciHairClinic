package com.example.vinciandroidv2.ui.main.profile

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.style.UnderlineSpan
import android.view.View
import androidx.core.text.inSpans
import androidx.core.view.isVisible
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.di.module.UserModule
import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.helper.RealmHelper.copyFromRealm
import com.example.vinciandroidv2.network.respons.User
import com.example.vinciandroidv2.ui.global.BaseFragment
import com.example.vinciandroidv2.ui.global.mvp.user.UserContract
import kotlinx.android.synthetic.main.fragment_notifications.*
import kotlinx.android.synthetic.main.item_row_text_view_switch_bottom_divider_sample_1.view.*
import toothpick.Scope
import toothpick.ktp.delegate.inject

class NotificationsFragment : BaseFragment(), UserContract.View {
    override val layoutRes = R.layout.fragment_notifications
    override val TAG = "ChangePasswordFragment"

    private val userPresenter: UserContract.Presenter by inject()
    override fun installModules(scope: Scope) {
        scope.installModules(UserModule(this))
    }

    private val userListener= RealmHelper.realm?.where(User::class.java)?.findAll()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        backButton?.setOnClickListener { activity?.onBackPressed() }

        saveButton?.apply {
            isSelected = true
            setOnClickListener {
                RealmHelper.getUserData()?.copyFromRealm()?.let { u ->
                    u.isShowNewsAndUpdatesNotification =
                        this@NotificationsFragment.includeNewsAndUpdatesCard?.switchView?.isChecked
                            ?: true
                    u.isShowPromotionsAndOffersNotification =
                        this@NotificationsFragment.includePromotionsAndOffersCard?.switchView?.isChecked
                            ?: true
                    u.isShowInsightsAndTipsNotification =
                        this@NotificationsFragment.includeInsightsAndTipsCard?.switchView?.isChecked
                            ?: true
                    u.isShowNewArticlesNotification =
                        this@NotificationsFragment.includeNewArticlesCard?.switchView?.isChecked
                            ?: true
                    u.isShowRequestsAndTicketsNotification =
                        this@NotificationsFragment.includeRequestsAndTicketsCard?.switchView?.isChecked
                            ?: true
                    userPresenter.editUser(u)
                }
            }
        }

        includeNewsAndUpdatesCard?.dividerTop?.isVisible = true
        includeNewsAndUpdatesCard?.initCard()
        includePromotionsAndOffersCard?.initCard()
        includeInsightsAndTipsCard?.initCard()
        includeNewArticlesCard?.initCard()
        includeRequestsAndTicketsCard?.initCard()

        userListener?.addChangeListener { _ -> updateViews() }
        updateViews()
    }

    private fun View.initCard() {
        switchView?.isClickable = false
        setOnClickListener { switchView?.apply { isChecked = !isChecked } }
    }

    private fun updateViews() {
        RealmHelper.getUserData()?.let { user ->
            includeNewsAndUpdatesCard?.switchView?.isChecked =
                user.isShowNewsAndUpdatesNotification
            includePromotionsAndOffersCard?.switchView?.isChecked =
                user.isShowPromotionsAndOffersNotification
            includeInsightsAndTipsCard?.switchView?.isChecked =
                user.isShowInsightsAndTipsNotification
            includeNewArticlesCard?.switchView?.isChecked =
                user.isShowNewArticlesNotification
            includeRequestsAndTicketsCard?.switchView?.isChecked =
                user.isShowRequestsAndTicketsNotification
        }
    }

    override fun setLocalization() {
        super.setLocalization()
        saveButton?.text = SpannableStringBuilder().inSpans(UnderlineSpan()) {
            append(RealmHelper.getLocalizedText("profile.notifications.button.save.text"))
        }
        titleField?.text = RealmHelper.getLocalizedText("profile.notifications.title.text")
        includeNewsAndUpdatesCard?.textField?.text =
            RealmHelper.getLocalizedText("profile.notifications.view.text.one")
        includePromotionsAndOffersCard?.textField?.text =
            RealmHelper.getLocalizedText("profile.notifications.view.text.two")
        includeInsightsAndTipsCard?.textField?.text =
            RealmHelper.getLocalizedText("profile.notifications.view.text.three")
        includeNewArticlesCard?.textField?.text =
            RealmHelper.getLocalizedText("profile.notifications.view.text.four")
        includeRequestsAndTicketsCard?.textField?.text =
            RealmHelper.getLocalizedText("profile.notifications.view.text.five")
    }

    override fun startLoading() {
        super<BaseFragment>.startLoading()
        saveButton?.isVisible = false
        saveLotti?.playAnimation()
        saveLotti?.isVisible = true

        viewsAreEnabled(false)
    }

    override fun stopLoading() {
        super<BaseFragment>.stopLoading()
        saveButton?.isVisible = true
        saveLotti?.isVisible = false
        saveLotti?.cancelAnimation()

        viewsAreEnabled(true)
    }

    private fun viewsAreEnabled(isEnabled: Boolean) {
        includeNewsAndUpdatesCard?.isEnabled = isEnabled
        includePromotionsAndOffersCard?.isEnabled = isEnabled
        includeInsightsAndTipsCard?.isEnabled = isEnabled
        includeNewArticlesCard?.isEnabled = isEnabled
        includeRequestsAndTicketsCard?.isEnabled = isEnabled
    }

    override fun onDestroyView() {
        super.onDestroyView()
        userListener?.removeAllChangeListeners()
    }
}