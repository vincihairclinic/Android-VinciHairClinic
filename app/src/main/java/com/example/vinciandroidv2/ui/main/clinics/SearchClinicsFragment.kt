package com.example.vinciandroidv2.ui.main.clinics

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.network.respons.Clinic
import com.example.vinciandroidv2.ui.Screens
import com.example.vinciandroidv2.ui.global.BaseFragment
import com.example.vinciandroidv2.ui.global.dp
import kotlinx.android.synthetic.main.fragment_clinics.includeSearchContainer
import kotlinx.android.synthetic.main.fragment_clinics.recyclerView
import kotlinx.android.synthetic.main.fragment_search_clinics.*
import kotlinx.android.synthetic.main.item_row_card_view_middle_two_text_view_location_view_sample_three.view.*
import kotlinx.android.synthetic.main.view_search_container.view.*

class SearchClinicsFragment : BaseFragment() {
    override val layoutRes = R.layout.fragment_search_clinics
    override val TAG = "SearchClinicsFragment"

    private val adapter: Adapter by lazy { Adapter() }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        includeSearchContainer?.apply {
            isSelected = true
            cancelButton?.apply {
                isVisible = true
                setOnClickListener { activity?.onBackPressed() }
            }
            searchBottomDivider?.isVisible = true
            searchTextField?.doOnTextChanged { text, start, before, count ->
                if (0 == text?.length) {
                    showNoResultsView(false, text.toString())
                    adapter.updateList(ArrayList())
                } else {
                    RealmHelper.realm?.where(Clinic::class.java)
                        ?.findAll()
                        ?.filter { it.name?.contains(text ?: "", true) == true }
                        ?.filterNotNull()
                        ?.let {
                            showNoResultsView(it.isEmpty(), text?.toString() ?: "")
                            adapter.updateList(arrayListOf<Clinic>().apply { addAll(it) })
                        }
                }
            }
        }

        recyclerView?.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = this@SearchClinicsFragment.adapter
        }
    }

    override fun setLocalization() {
        super.setLocalization()
        includeSearchContainer?.apply {
            searchTextField?.hint = RealmHelper.getLocalizedText("clinics.search.field.search.hint")
            cancelButton?.text = RealmHelper.getLocalizedText("clinics.search.button.cancel.text")
        }
    }

    private fun showNoResultsView(isShow: Boolean = true, query: String = "") {
        noResultsContainer?.apply {
            isVisible = isShow
            noResultsTitleTextField?.text =
                RealmHelper.getLocalizedText("clinics.search.noresults.title.text")
            noResultsDescriptionTextField?.text = StringBuilder()
                .append(RealmHelper.getLocalizedText("clinics.search.noresults.description.text"))
                .append(" ")
                .append(query)
                .append(".")
        }
    }

    inner class Adapter : RecyclerView.Adapter<Adapter.Holder>() {
        val list = ArrayList<Clinic>()

        @SuppressLint("NotifyDataSetChanged")
        internal fun updateList(list: List<Clinic>) {
            this.list.clear()
            this.list.addAll(list)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(
            LayoutInflater.from(context).inflate(
                R.layout.item_row_card_view_middle_two_text_view_location_view_sample_three,
                parent,
                false
            ).apply {
                this?.image?.updateLayoutParams<ConstraintLayout.LayoutParams> {
                    setMargins(24.dp, 12.dp, 24.dp, 12.dp)
                }
                this?.addressTextField?.updateLayoutParams<ConstraintLayout.LayoutParams> {
                    bottomMargin = 12.dp
                }
                this?.addressTextField?.maxLines = 3
            }
        )

        override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(list[position])

        override fun getItemCount() = list.size

        inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(clinic: Clinic) {
                Glide.with(itemView).load(clinic.urlImage).into(itemView.image)
                itemView.townTextField?.text = clinic.name
                itemView.addressTextField?.text = clinic.address
                itemView.locationMapButton?.setOnClickListener {
                    replace(R.id.content, Screens.Clinics.getClinicMapFragment())
                }
                itemView.setOnClickListener {
                    replace(
                        R.id.content,
                        Screens.Clinics.getExpandedClinicFragment(clinic.id)
                    )
                }
            }
        }
    }
}