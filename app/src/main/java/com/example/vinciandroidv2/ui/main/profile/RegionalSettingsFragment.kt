package com.example.vinciandroidv2.ui.main.profile

import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.style.UnderlineSpan
import androidx.core.text.inSpans
import androidx.core.view.isVisible
import com.example.vinciandroidv2.App
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.di.module.ListModule
import com.example.vinciandroidv2.di.module.UserModule
import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.helper.RealmHelper.copyFromRealm
import com.example.vinciandroidv2.helper.setEndDrawable
import com.example.vinciandroidv2.network.respons.Country
import com.example.vinciandroidv2.network.respons.Language
import com.example.vinciandroidv2.network.respons.User
import com.example.vinciandroidv2.ui.Screens
import com.example.vinciandroidv2.ui.global.BaseFragment
import com.example.vinciandroidv2.ui.global.mvp.list.ListContract
import com.example.vinciandroidv2.ui.global.mvp.user.UserContract
import com.example.vinciandroidv2.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_regional_settings.*
import kotlinx.android.synthetic.main.item_row_text_view_switch_bottom_divider_sample_1.view.dividerTop
import kotlinx.android.synthetic.main.item_row_two_text_view_bottom_divider_sample_1.view.*
import toothpick.Scope
import toothpick.ktp.delegate.inject

class RegionalSettingsFragment : BaseFragment(), ListContract.View, UserContract.View {
    override val layoutRes = R.layout.fragment_regional_settings
    override val TAG = "RegionalSettingsFragment"

    private val listPresenter: ListContract.Presenter by inject()
    private val userPresenter: UserContract.Presenter by inject()
    override fun installModules(scope: Scope) {
        scope.installModules(ListModule(this), UserModule(this))
    }

    private val userListener = RealmHelper.realm?.where(User::class.java)?.findAll()
    private var selectedCountryId = -1
        set(value) {
            field = value
            includeCountryCard?.field?.text = RealmHelper.realm?.where(Country::class.java)
                ?.equalTo("id", selectedCountryId)
                ?.findFirst()
                ?.name
        }
    private var selectedLanguageKey = ""
        set(value) {
            field = value
            includeLanguageCard?.field?.text = RealmHelper.realm?.where(Language::class.java)
                ?.equalTo("key", selectedLanguageKey)
                ?.findFirst()
                ?.name
        }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        backButton?.setOnClickListener { activity?.onBackPressed() }

        saveButton?.apply {
            isSelected = true
            setOnClickListener {
                if (-1 != this@RegionalSettingsFragment.selectedCountryId && "" != this@RegionalSettingsFragment.selectedLanguageKey) {

                    RealmHelper.getUserData()?.copyFromRealm()?.let { u ->
                        u.countryId = this@RegionalSettingsFragment.selectedCountryId
                        u.languageKey = this@RegionalSettingsFragment.selectedLanguageKey
                        userPresenter.editUser(u)
                    }
                }
            }
        }

        includeCountryCard?.dividerTop?.isVisible = true
        includeCountryCard?.field?.setEndDrawable(R.drawable.ic_arrow_down_brown_24)
        includeLanguageCard?.field?.setEndDrawable(R.drawable.ic_arrow_down_brown_24)

        includeCountryCard?.setOnClickListener {
            Screens.Profile.getCountrySelectionBottomSheet(selectedCountryId) { newId ->
                this@RegionalSettingsFragment.selectedCountryId = newId
            }.show(childFragmentManager, "CountrySelectionBottomSheet")
        }
        includeLanguageCard?.setOnClickListener {
            Screens.Profile.getLanguageSelectionBottomSheet(selectedLanguageKey) { newKey ->
                this@RegionalSettingsFragment.selectedLanguageKey = newKey
            }.show(childFragmentManager, "LanguageSelectionBottomSheet")
        }

        userListener?.addChangeListener { _ -> updateViews() }
        updateViews()
    }

    private fun updateViews() {
        RealmHelper.getUserData()?.let { user ->
            selectedCountryId = user.countryId ?: -1
            selectedLanguageKey = user.languageKey ?: ""
        }
    }

    override fun setLocalization() {
        super.setLocalization()
        saveButton?.text = SpannableStringBuilder().inSpans(UnderlineSpan()) {
            append(RealmHelper.getLocalizedText("profile.regional.button.save.text"))
        }
        titleField?.text = RealmHelper.getLocalizedText("profile.regional.title.text")
        includeCountryCard?.label?.text =
            RealmHelper.getLocalizedText("profile.regional.countryselector.text")
        includeLanguageCard?.label?.text =
            RealmHelper.getLocalizedText("profile.regional.languageselector.text")
    }

    override fun userLoaded() {
        com.example.vinciandroidv2.ui.global.host =
            RealmHelper.getHostFromSelectedCountry(selectedCountryId)
        (activity?.application as App).updateAppScope()
        RealmHelper.removeLastTimeStamp()
        listPresenter.getLocalization(RealmHelper.getUserData()?.languageKey ?: "en")
    }

    override fun localizationsLoaded() {
        listPresenter.getListsAfterLogin()
    }

    override fun listsAfterLoginLoaded() {
//        EventBus.getDefault().post(LocalizationEvent())
        startActivity(Intent(activity, MainActivity::class.java))
        activity?.finish()
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
        includeCountryCard?.isEnabled = isEnabled
        includeLanguageCard?.isEnabled = isEnabled
    }

    override fun onDestroyView() {
        super.onDestroyView()
        userListener?.removeAllChangeListeners()
    }
}