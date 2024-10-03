package com.example.vinciandroidv2.ui.localization

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vinciandroidv2.App
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.network.respons.Country
import com.example.vinciandroidv2.ui.Screens
import com.example.vinciandroidv2.ui.global.BaseFragment
import com.example.vinciandroidv2.ui.global.defaultHost
import com.example.vinciandroidv2.ui.global.selectedCountryId
import io.realm.RealmList
import kotlinx.android.synthetic.main.fragment_country_selection.*
import kotlinx.android.synthetic.main.item_row_drawable_text_view_check_sample_1.view.*
import kotlinx.android.synthetic.main.view_next_button.view.*

class CountrySelectionFragment : BaseFragment() {
    override val layoutRes = R.layout.fragment_country_selection
    override val TAG = "CountrySelectionFragment"

    private val list = RealmHelper.realm?.where(Country::class.java)?.findAll() ?: RealmList()

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
        if (-1 != selectedCountryId) {
            includeNextButton?.isSelected = true
            includeNextButton?.setOnClickListener {
                (activity?.application as App).updateAppScope()
                replace(android.R.id.content, Screens.Localization.getLanguageSelectionFragment())
            }
        } else {
            includeNextButton?.isSelected = false
            includeNextButton?.setOnClickListener(null)
        }
    }


    override fun setLocalization() {
        super.setLocalization()
        countrySelectionTitle?.text = RealmHelper.getLocalizedText("countryselection.title.text")
        includeNextButton?.nextButtonText?.text =
            RealmHelper.getLocalizedText("countryselection.button.next.text")
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
            fun bind(country: Country) {
                Glide.with(itemView).load(country.urlFlagImage).into(itemView.startDrawable)
                itemView.textField?.text = country.name
                (selectedCountryId == country.id).let { isSelected ->
                    itemView.isSelected = isSelected
                    itemView.checkDrawable?.isVisible = isSelected
                }
                itemView.setOnClickListener {
                    selectedCountryId = country.id
                    com.example.vinciandroidv2.ui.global.host = country.host ?: defaultHost
                    notifyDataSetChanged()
                    updateNextButton()
                }
            }
        }
    }
}