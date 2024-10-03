package com.example.vinciandroidv2.ui.main.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.inSpans
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.network.respons.Country
import com.example.vinciandroidv2.ui.global.BaseBottomSheetFragment
import io.realm.RealmList
import kotlinx.android.synthetic.main.bottom_sheet_country_selection.*
import kotlinx.android.synthetic.main.item_row_drawable_text_view_check_sample_1.view.*

class CountrySelectionBottomSheet(
    private var selectedCountryId: Int,
    private val callback: (newCountryId: Int) -> Unit
) : BaseBottomSheetFragment() {
    override val layoutRes = R.layout.bottom_sheet_country_selection
    override val TAG = "CountrySelectionBottomSheet"

    private val list = RealmHelper.realm?.where(Country::class.java)?.findAll() ?: RealmList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        titleField?.text =
            RealmHelper.getLocalizedText("profile.regional.bottomsheet.country.title.text")

        cancelButton?.apply {
            isSelected = true
            text = SpannableStringBuilder().inSpans(UnderlineSpan()) {
                append(RealmHelper.getLocalizedText("profile.regional.bottomsheet.country.button.cancel.text"))
            }
            setOnClickListener { dismiss() }
        }
        findButton(R.id.saveButton)?.apply {
            setText(
                RealmHelper.getLocalizedText("profile.regional.bottomsheet.country.button.save.text"),
                R.color.text_white_FFFFFF,
                R.font.montserrat_semi_bold
            )
            setBackgroundColor(R.color.primary_brown_7D5F2B)
            onClick {
                callback(selectedCountryId)
                dismiss()
            }
        }

        recyclerView?.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = Adapter()
        }
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
                    notifyDataSetChanged()
                }
            }
        }
    }
}