package com.example.vinciandroidv2.ui.localization

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.di.module.ListModule
import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.network.respons.Language
import com.example.vinciandroidv2.ui.Screens
import com.example.vinciandroidv2.ui.auth.AuthActivity
import com.example.vinciandroidv2.ui.global.BaseFragment
import com.example.vinciandroidv2.ui.global.mvp.list.ListContract
import com.example.vinciandroidv2.ui.global.selectedLanguageKey
import io.realm.RealmList
import kotlinx.android.synthetic.main.fragment_country_selection.*
import kotlinx.android.synthetic.main.item_row_drawable_text_view_check_sample_1.view.*
import kotlinx.android.synthetic.main.view_next_button.view.*
import toothpick.Scope
import toothpick.ktp.delegate.inject

class LanguageSelectionFragment : BaseFragment(), ListContract.View {
    override val layoutRes = R.layout.fragment_country_selection
    override val TAG = "LanguageSelectionFragment"

    private val listPresenter: ListContract.Presenter by inject()
    override fun installModules(scope: Scope) {
        scope.installModules(ListModule(this))
    }

    private val list = RealmHelper.realm?.where(Language::class.java)?.findAll() ?: RealmList()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        countryListContainer?.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = Adapter()
        }
        updateNextButton()
    }

    private fun updateNextButton() {
        if ("" != selectedLanguageKey) {
            includeNextButton?.isSelected = true
            includeNextButton?.setOnClickListener {
                RealmHelper.removeLastTimeStamp()
                listPresenter.getLocalization(selectedLanguageKey)
            }
        } else {
            includeNextButton?.isSelected = false
            includeNextButton?.setOnClickListener(null)
        }
    }

    override fun localizationsLoaded() {
        super.localizationsLoaded()
        startActivity(Intent(activity, AuthActivity::class.java))
        activity?.finish()
    }

    override fun setLocalization() {
        super.setLocalization()
        countrySelectionTitle?.text = RealmHelper.getLocalizedText("languageselection.title.text")
        includeNextButton?.nextButtonText?.text = RealmHelper.getLocalizedText("languageselection.button.next.text")
    }

    inner class Adapter : RecyclerView.Adapter<Adapter.Holder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(
            LayoutInflater.from(context).inflate(
                R.layout.item_row_drawable_text_view_check_sample_1, parent, false
            )
        )

        override fun onBindViewHolder(holder: Holder, position: Int) =
            holder.bind(list[position])

        override fun getItemCount() = list.size

        inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            @SuppressLint("NotifyDataSetChanged")
            fun bind(language: Language) {
                Glide.with(itemView).load(language.urlImage).into(itemView.startDrawable)
                itemView.textField?.text = language.name
                (selectedLanguageKey == language.key).let { isSelected ->
                    itemView.isSelected = isSelected
                    itemView.checkDrawable?.isVisible = isSelected
                }
                itemView.setOnClickListener {
                    selectedLanguageKey = language.key ?: ""
                    notifyDataSetChanged()
                    updateNextButton()
                }
            }
        }
    }

    override fun startLoading() {
        super<BaseFragment>.startLoading()
        includeNextButton?.isEnabled = false
        includeNextButton?.nextButtonText?.isVisible = false
        includeNextButton?.nextButtonLottie?.playAnimation()
        includeNextButton?.nextButtonLottie?.isVisible = true
    }

    override fun stopLoading() {
        super<BaseFragment>.stopLoading()
        includeNextButton?.isEnabled = true
        includeNextButton?.nextButtonText?.isVisible = true
        includeNextButton?.nextButtonLottie?.cancelAnimation()
        includeNextButton?.nextButtonLottie?.isVisible = false
    }
}

