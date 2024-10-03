package com.example.vinciandroidv2.ui.main.store

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.network.respons.ShopNowUrl
import com.example.vinciandroidv2.ui.global.BaseBottomSheetFragment
import kotlinx.android.synthetic.main.bottom_sheet_country_selection_sample_two.*
import kotlinx.android.synthetic.main.item_row_drawable_text_view_check_sample_1.view.*

class StoreSelectionBottomSheet(
    private val list: ArrayList<ShopNowUrl>,
    private val callback: (newSelectedUrlLink: String) -> Unit
) :
    BaseBottomSheetFragment() {
    override val layoutRes = R.layout.bottom_sheet_country_selection_sample_two
    override val TAG = "StoreSelectionBottomSheet"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textHtml = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(RealmHelper.getLocalizedText("store.main.bottomsheet.selectstore.title.text") ?: "", Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(RealmHelper.getLocalizedText("store.main.bottomsheet.selectstore.title.text") ?: "")
        }
        titleField?.text = textHtml


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
            fun bind(shopNowUrl: ShopNowUrl) {
                RealmHelper.getCountryById(shopNowUrl.countryId)?.let { country ->
                    Glide.with(itemView).load(country.urlFlagImage).into(itemView.startDrawable)
                    itemView.textField?.text = country.name
                    itemView.setOnClickListener {
                        callback(shopNowUrl.shopNowUrl ?: "")
                        dismiss()
                    }
                }
            }
        }
    }
}