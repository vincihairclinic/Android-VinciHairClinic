package com.example.vinciandroidv2.ui.main.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.network.respons.Gender
import com.example.vinciandroidv2.network.respons.ProcedureResult
import com.example.vinciandroidv2.network.respons.ProcedureResultVideo
import com.example.vinciandroidv2.ui.Screens
import com.example.vinciandroidv2.ui.global.BaseFragment
import com.example.vinciandroidv2.ui.global.changeFormat
import com.example.vinciandroidv2.ui.global.dp
import com.example.vinciandroidv2.ui.global.initViewPagerWithPageIndicator
import com.example.vinciandroidv2.ui.main.FullScreenVideoActivity
import com.example.vinciandroidv2.ui.main.VIDEO_URL_EXTRA
import com.makeramen.roundedimageview.RoundedImageView
import io.realm.RealmList
import kotlinx.android.synthetic.main.fragment_expanded_result.*
import kotlinx.android.synthetic.main.view_card_image_before_after_result.view.*

class ExpandedResultFragment(private val procedureResult: ProcedureResult) : BaseFragment() {
    override val layoutRes = R.layout.fragment_expanded_result
    override val TAG = "ExpandedResultFragment"
    private val genderList = RealmHelper.realm?.where(Gender::class.java)?.findAll() ?: RealmList()
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        backButton?.setOnClickListener { activity?.onBackPressed() }
        val arrayList = ArrayList<String>()
        procedureResult.urlImage?.let { arrayList.add(it) }
        image.setOnClickListener {
            replace(
                R.id.content,
                Screens.Home.getFullScreenImageFragment(arrayList ?: ArrayList())
            )
        }
        Glide.with(this).load(procedureResult.urlImage).into(image)
        titleTextField?.text = procedureResult.title
        subTitleTextField?.text = procedureResult.subtitle
        treatmentTextField?.text = procedureResult.procedure?.name
        dateTextField?.text = procedureResult.date?.changeFormat("dd MMMM, yyyy")

        genderTextField?.text = genderList.firstOrNull { it.id == procedureResult.genderId }?.name

        val textHtml = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(procedureResult.content ?: "", Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(procedureResult.content ?: "")
        }
        mainTextField?.text = textHtml

        procedureResult.procedureResultVideoList?.let { list ->
            videoRecyclerView?.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                setHasFixedSize(true)
                adapter = VideoAdapter(list)
            }
        } ?: run { videoRecyclerView?.isVisible = false }

        procedureResult.procedureResultImageList?.forEach { imageItem ->
            val view = LayoutInflater.from(context).inflate(
                R.layout.view_card_image_before_after_result,
                beforeAfterResultImageContainer,
                false
            )
            ImageAdapter(
                arrayListOf(imageItem.urlBeforeImage ?: "", imageItem.urlAfterImage ?: "")
            ).initViewPagerWithPageIndicator(
                view?.afterResultsViewPager, view?.pageIndicator, null
            )
            beforeAfterResultImageContainer?.addView(view)
        }
    }

    override fun setLocalization() {
        super.setLocalization()
        treatmentLabel?.text =
            RealmHelper.getLocalizedText("home.result.expanded.category.title.text")
        dateLabel?.text = RealmHelper.getLocalizedText("home.result.expanded.date.title.text")
        titleTextFieldTwo?.text =
            RealmHelper.getLocalizedText("home.result.expanded.casestudy.title.text")
        beforeAfterResultsLabel?.text =
            RealmHelper.getLocalizedText("home.result.expanded.beforeafter.title.text")
        beforeAfterResultsLabel2?.text =
            RealmHelper.getLocalizedText("home.result.expanded.beforeafter.subtitle")
        genderLabel?.text = RealmHelper.getLocalizedText("profile.personalinfo.gender.label.text")
    }

    inner class VideoAdapter(private val videoList: ArrayList<ProcedureResultVideo>) :
        RecyclerView.Adapter<VideoAdapter.Holder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            Holder(
                LayoutInflater.from(context).inflate(
                    R.layout.item_row_card_view_middle_one_text_view_play_button_sample_one,
                    parent,
                    false
                )
            )

        override fun onBindViewHolder(holder: VideoAdapter.Holder, position: Int) =
            holder.bind(videoList[position])

        override fun getItemCount() = videoList.size

        inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            @SuppressLint("NotifyDataSetChanged")
            fun bind(procedureResultVideo: ProcedureResultVideo) {
                itemView.findViewById<RoundedImageView>(R.id.image)?.apply {
                    updateLayoutParams<ConstraintLayout.LayoutParams> {
                        marginStart = 6.dp
                        marginEnd = 6.dp
                    }
                    Glide.with(this)
                        .load(procedureResultVideo.urlVideo)
                        .placeholder(R.drawable.background_linear_light)
                        .into(this)
                }
                itemView.findViewById<TextView>(R.id.textField)?.text = procedureResultVideo.name
                itemView.setOnClickListener {
                    startActivity(
                        Intent(activity, FullScreenVideoActivity::class.java)
                            .putExtra(VIDEO_URL_EXTRA, procedureResultVideo.urlVideo)
                    )
                }
            }
        }
    }

    inner class ImageAdapter(private val imageList: ArrayList<String>) :
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
                itemView.findViewById<RoundedImageView>(R.id.image)?.apply {
                    this.setOnClickListener {
                        replace(
                            R.id.content,
                            Screens.Home.getFullScreenImageFragment(imageList ?: ArrayList())
                        )
                    }
                    Glide.with(this).load(urlImage).into(this)
                }
            }
        }
    }
}