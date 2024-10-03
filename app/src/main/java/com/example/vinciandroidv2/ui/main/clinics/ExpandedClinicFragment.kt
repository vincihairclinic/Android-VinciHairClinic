package com.example.vinciandroidv2.ui.main.clinics

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.util.Base64
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.helper.setStartDrawable
import com.example.vinciandroidv2.network.respons.Clinic
import com.example.vinciandroidv2.ui.Screens
import com.example.vinciandroidv2.ui.global.BaseFragment
import com.example.vinciandroidv2.ui.global.dp
import com.example.vinciandroidv2.ui.global.getTypeFace
import com.example.vinciandroidv2.ui.global.initViewPagerWithPageIndicator
import io.realm.RealmList
import kotlinx.android.synthetic.main.fragment_expanded_clinic.*
import kotlinx.android.synthetic.main.view_card_button_sample_two.view.*
import kotlinx.android.synthetic.main.view_product_image.view.*

class ExpandedClinicFragment(private val clinicId: Int) : BaseFragment() {
    constructor() : this(-1)

    override val layoutRes = R.layout.fragment_expanded_clinic
    override val TAG = "ExpandedClinicFragment"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        backButton?.setOnClickListener { activity?.onBackPressed() }

        RealmHelper.realm?.where(Clinic::class.java)
            ?.equalTo("id", clinicId)
            ?.findFirst()
            ?.let { clinic ->
                shareButton?.setOnClickListener {

                }

                val imageList = RealmList<String>().apply {
                    add(clinic.urlImage)
                    addAll(clinic.urlImageList ?: RealmList())
                }
                ImageAdapter(imageList)
                    .initViewPagerWithPageIndicator(imageViewPager, pageIndicator, null)
//                val span: Spannable = SpannableString(
//                    "   " + if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                        Html.fromHtml(clinic.about ?: "", Html.FROM_HTML_MODE_COMPACT)
//                    } else {
//                        Html.fromHtml(clinic.about ?: "")
//                    } + "   "
//                )
                val textHtml =if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { Html.fromHtml(clinic.about?: "", Html.FROM_HTML_MODE_COMPACT)}else{
                    Html.fromHtml(clinic.about ?: "")
                }
                titleTextField?.text = clinic.name
                mainTextField?.text = textHtml

                clinic.benefitList?.forEach {
                    TextView(context).apply {
                        setStartDrawable(R.drawable.ic_check_brown_24)
                        compoundDrawablePadding = 8.dp
                        text = it ?: ""
                        setTextColor(resources.getColor(R.color.text_dark_0E1012))
                        setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
                        typeface = getTypeFace(context, R.font.montserrat_bold)
                    }.also { benefit -> benefitsContainer?.addView(benefit) }
                }

                clinicLocationDescriptionTextField?.text = clinic.aboutClinicLocation
                clinicAddressTextField?.apply {
                    text = clinic.address
                    setOnClickListener {
                        replace(
                            R.id.content,
                            Screens.Clinics.getClinicMapFragment(clinic.id)
                        )
                    }
                }

                includeDirectionsCard?.apply {
                    startDrawable?.setImageResource(R.drawable.ic_send_white_16)
                    startDrawable?.layoutParams?.width = 20.dp
                    startDrawable?.layoutParams?.height = 20.dp
                    setOnClickListener {
                        val uri =
                            Uri.parse("https://www.google.com/maps/dir/?api=1&destination=${clinic.lat},${clinic.lng}")
                        startActivity(Intent(Intent.ACTION_VIEW, uri))
                    }
                }
                includeContactCard?.apply {
                    startDrawable?.setImageResource(R.drawable.ic_phone_brown_24)
                    startDrawable?.setColorFilter(Color.parseColor("#ffffff"))
                    startDrawable?.layoutParams?.width = 20.dp
                    startDrawable?.layoutParams?.height = 20.dp
                    setOnClickListener {
                        Screens.Clinics.getContactBottomSheet(clinicId)
                            .show(childFragmentManager, ContactBottomSheet().TAG)
                    }
                }
            }
    }

    override fun setLocalization() {
        super.setLocalization()
        clinicLocationTextField?.text =
            RealmHelper.getLocalizedText("clinics.expanded.clinic.subtitle.text")
        includeDirectionsCard?.buttonCardTextField?.text =
            RealmHelper.getLocalizedText("clinics.expanded.clinic.button.directions.text")
        includeContactCard?.buttonCardTextField?.text =
            RealmHelper.getLocalizedText("clinics.expanded.clinic.button.contact.text")
    }

    inner class ImageAdapter(private val imageList: RealmList<String>) :
        RecyclerView.Adapter<ImageAdapter.Holder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(
            LayoutInflater.from(context).inflate(R.layout.view_product_image, parent, false)
        )

        override fun onBindViewHolder(holder: Holder, position: Int) {
            imageList.getOrNull(position)?.let { holder.bind(it) }
        }

        override fun getItemCount() = imageList.size

        inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            @SuppressLint("NotifyDataSetChanged")
            fun bind(urlImage: String) {
                Glide.with(itemView).load(urlImage).into(itemView.image)
            }
        }
    }
}