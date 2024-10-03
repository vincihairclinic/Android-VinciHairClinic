package com.example.vinciandroidv2.ui.main.clinics

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.network.respons.Clinic
import com.example.vinciandroidv2.network.respons.Country
import com.example.vinciandroidv2.network.respons.Territory
import com.example.vinciandroidv2.ui.Screens
import com.example.vinciandroidv2.ui.global.BaseFragment
import com.example.vinciandroidv2.ui.global.dp
import com.example.vinciandroidv2.ui.main.profile.ProfileBottomSheet
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_clinics.*
import kotlinx.android.synthetic.main.item_row_card_view_middle_two_text_view_location_view_sample_three.view.*
import kotlinx.android.synthetic.main.view_card_button_sample_two.view.*
import kotlinx.android.synthetic.main.view_search_container.view.*

class ClinicsFragment : BaseFragment() {
    override val layoutRes = R.layout.fragment_clinics
    override val TAG = "ClinicsFragment"

    private val adapter: Adapter by lazy { Adapter() }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        profileMenuButton?.setOnClickListener {
            Screens.Profile.getProfileBottomSheet()
                .show(childFragmentManager, ProfileBottomSheet().TAG)
        }

        includeSearchContainer?.apply {
            searchTextField?.isFocusable = false
            searchTextField?.isClickable = true
            searchTextField?.setOnClickListener {
                replace(R.id.content, Screens.Clinics.getSearchClinicsFragment())
            }
        }

        recyclerView?.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = this@ClinicsFragment.adapter
        }

        includeNearMeButton?.apply {
            mainCardContainer?.setCardBackgroundColor(Color.parseColor("#0E1012"))
            drawableCardContainer?.setCardBackgroundColor(Color.parseColor("#495057"))
            startDrawable?.setImageResource(R.drawable.ic_map_white_20)
            setOnClickListener {
                replace(R.id.content, Screens.Clinics.getClinicMapFragment())
            }
        }

        initTabLayout()
    }

    private fun initTabLayout() {
        tabLayout?.removeAllTabs()

        RealmHelper.realm?.where(Territory::class.java)?.findAll()?.forEach {
            tabLayout?.newTab()?.apply {
                id = it.id
                text = it.name
                Glide.with(this@ClinicsFragment)
                    .asBitmap()
                    .load(it.urlAreaImage)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {
                            icon = BitmapDrawable(resources, resource)
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {}
                    })
            }?.also { tabLayout?.addTab(it) }
        }
        tabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onTabSelected(tab: TabLayout.Tab?) {
                activity?.getColor(R.color.primary_brown_7D5F2B)?.let {
                    tab?.icon?.setTint(it)
                }
                RealmHelper.realm?.where(Clinic::class.java)
                    ?.findAll()
                    ?.filter { it.territoryId == tab?.id }
                    ?.filterNotNull()
                    ?.let { adapter.updateList(arrayListOf<Clinic>().apply { addAll(it) }) }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                activity?.getColor(R.color.text_light_495057_65)?.let {
                    tab?.icon?.setTint(it)
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                onTabSelected(tab)
            }
        })
        tabLayout?.getTabAt(0)?.select()
    }

    override fun setLocalization() {
        super.setLocalization()
        clinicTitleField?.text = RealmHelper.getLocalizedText("clinics.main.title.text")
        includeSearchContainer?.searchTextField?.hint =
            RealmHelper.getLocalizedText("clinics.main.field.search.hint")
        includeNearMeButton?.buttonCardTextField?.text =
            RealmHelper.getLocalizedText("clinics.main.button.nearme.text")
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
                    replace(R.id.content, Screens.Clinics.getClinicMapFragment(clinic.id))
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