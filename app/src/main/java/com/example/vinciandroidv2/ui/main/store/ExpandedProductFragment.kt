package com.example.vinciandroidv2.ui.main.store

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.network.respons.BaseProduct
import com.example.vinciandroidv2.network.respons.ProductReview
import com.example.vinciandroidv2.ui.Screens
import com.example.vinciandroidv2.ui.global.dp
import com.example.vinciandroidv2.ui.global.getScreenWidth
import com.example.vinciandroidv2.ui.global.initViewPagerWithPageIndicator
import com.example.vinciandroidv2.ui.main.FullScreenVideoActivity
import com.example.vinciandroidv2.ui.main.VIDEO_URL_EXTRA
import kotlinx.android.synthetic.main.fragment_expanded_product.*
import kotlinx.android.synthetic.main.item_row_card_view_big_sample_two.view.*
import kotlinx.android.synthetic.main.item_row_card_view_middle_one_text_view_play_button_sample_one.view.*
import kotlinx.android.synthetic.main.item_row_faq_question_expandable_answer_sample_one.view.*
import kotlinx.android.synthetic.main.view_card_button_sample_two.view.*
import kotlinx.android.synthetic.main.view_product_image.view.image

class ExpandedProductFragment(private val productId: Int) : BaseStoreFragment() {
    override val layoutRes = R.layout.fragment_expanded_product
    override val TAG = "ExpandedProductFragment"

    private val whatPeopleAreSayingAdapter: ReviewAdapter by lazy { ReviewAdapter() }
    private var whatPeopleAreSayingList = ArrayList<ProductReview>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        backButton?.setOnClickListener { activity?.onBackPressed() }
        productPresenter.getProductById(productId)

        LayoutInflater.from(context).inflate(
            R.layout.item_row_card_view_middle_drawable_two_text_view_sample_four,
            bottomCardListContainer,
            false
        )?.apply {
            findViewById<ImageView>(R.id.image)?.apply {
                Glide.with(this).load(R.drawable.ic_bolt_brown_24).into(this)
            }
        }?.also { bottomCardListContainer?.addView(it) }

        LayoutInflater.from(context).inflate(
            R.layout.item_row_card_view_middle_drawable_two_text_view_sample_four,
            bottomCardListContainer,
            false
        )?.apply {
            findViewById<ImageView>(R.id.image)?.apply {
                Glide.with(this).load(R.drawable.ic_shield_brown_24).into(this)
            }
        }?.also { bottomCardListContainer?.addView(it) }

        LayoutInflater.from(context).inflate(
            R.layout.item_row_card_view_middle_drawable_two_text_view_sample_four,
            bottomCardListContainer,
            false
        )?.apply {
            findViewById<ImageView>(R.id.image)?.apply {
                Glide.with(this).load(R.drawable.ic_smartphone_brown_24).into(this)
            }
        }?.also { bottomCardListContainer?.addView(it) }
    }

    override fun productByIdLoaded(baseProduct: BaseProduct) {
        baseProduct.product?.let { product ->

            shareButton?.setOnClickListener {

            }

            ImageAdapter(product.urlImageList ?: ArrayList())
                .initViewPagerWithPageIndicator(imageViewPager, pageIndicator, null)

            includedShopNowCard?.apply {
                startDrawable?.setImageResource(R.drawable.ic_shop_white_18)
                setOnClickListener {
                    Screens.Store.getStoreSelectionBottomSheet(
                        baseProduct.product?.shopNowUrlList ?: ArrayList()
                    ) { shopUrl ->
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(shopUrl)))
                    }.show(childFragmentManager, "StoreSelectionBottomSheet")
                }
            }

            titleTextField?.text = product.name
            mainTextField?.text = product.aboutProduct

            product.urlVideo?.let {
                includeBigVideoCard?.isVisible = true
                includeBigVideoCard?.imageContainer?.let { imageContainer ->
                    Glide.with(this)
                        .load(product.urlVideo)
                        .placeholder(R.drawable.background_linear_light)
                        .into(imageContainer)
                }
                includeBigVideoCard?.titleTextField?.text = product.videoName
                includeBigVideoCard?.setOnClickListener {
                    startActivity(
                        Intent(activity, FullScreenVideoActivity::class.java)
                            .putExtra(VIDEO_URL_EXTRA, product.urlVideo)
                    )
                }
            }

            if (0 != product.productReviewList?.size) {
                whatPeopleAreSayingTextLabel?.isVisible = true
                peopleSayingRecycler?.isVisible = true
                whatPeopleAreSayingList.addAll(product.productReviewList ?: ArrayList())
                peopleSayingRecycler?.apply {
                    layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    setHasFixedSize(true)
                    adapter = this@ExpandedProductFragment.whatPeopleAreSayingAdapter
                }
            }

            if (0 != product.faqs?.size) {
                faqTextLabel?.isVisible = true
                faqContainer?.isVisible = true
                product.faqs?.forEach { faq ->
                    LayoutInflater.from(context).inflate(
                        R.layout.item_row_faq_question_expandable_answer_sample_one,
                        faqContainer,
                        false
                    )?.apply {
                        questionTextField?.text = faq.question
                        answerTextField?.text = faq.answer
                        setOnClickListener {
                            isSelected = !isSelected
                            answerTextField?.isVisible = isSelected
                        }
                    }?.also { faqContainer?.addView(it) }
                }
            }

            bottomCardListContainer?.isVisible = true
        }
    }

    override fun setLocalization() {
        super.setLocalization()
        includedShopNowCard?.buttonCardTextField?.text =
            RealmHelper.getLocalizedText("store.expanded.product.button.shopnow.text")
        descriptionTextLabel?.text =
            RealmHelper.getLocalizedText("store.expanded.product.subtitle.text")
        whatPeopleAreSayingTextLabel?.text =
            RealmHelper.getLocalizedText("store.exanded.product.subtitle.two.text")
        faqTextLabel?.text =
            RealmHelper.getLocalizedText("store.expanded.product.subtitle.three.text")

        bottomCardListContainer?.apply {
            getChildAt(0)?.titleTextField?.text =
                RealmHelper.getLocalizedText("store.expanded.product.bottomcard.one.text.one")
            getChildAt(0)?.textField?.text =
                RealmHelper.getLocalizedText("store.expanded.product.bottomcard.one.text.two")
            getChildAt(1)?.titleTextField?.text =
                RealmHelper.getLocalizedText("store.expanded.product.bottomcard.two.text.one")
            getChildAt(1)?.textField?.text =
                RealmHelper.getLocalizedText("store.expanded.product.bottomcard.two.text.two")
            getChildAt(2)?.titleTextField?.text =
                RealmHelper.getLocalizedText("store.expanded.product.bottomcard.three.text.one")
            getChildAt(2)?.textField?.text =
                RealmHelper.getLocalizedText("store.expanded.product.bottomcard.one.three.two")
        }
    }

    override fun startLoading() {
        super.startLoading()
    }

    override fun stopLoading() {
        super.stopLoading()
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
                Glide.with(itemView).load(urlImage).into(itemView.image)
            }
        }
    }

    inner class ReviewAdapter : RecyclerView.Adapter<ReviewAdapter.Holder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(
            LayoutInflater.from(context).inflate(
                R.layout.item_row_card_view_middle_one_text_view_play_button_sample_one,
                parent,
                false
            ).apply {
                this?.updateLayoutParams {
                    width = ConstraintLayout.LayoutParams.WRAP_CONTENT
                }
                this?.image?.updateLayoutParams<ConstraintLayout.LayoutParams> {
                    width = (getScreenWidth(activity) * 0.578).toInt()
                    height = (getScreenWidth(activity) * 0.373).toInt()
                    marginStart = 6.dp
                    marginEnd = 6.dp
                }
            }
        )

        override fun onBindViewHolder(holder: Holder, position: Int) =
            holder.bind(whatPeopleAreSayingList[position])

        override fun getItemCount() = whatPeopleAreSayingList.size

        inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            @SuppressLint("NotifyDataSetChanged")
            fun bind(productReview: ProductReview) {
                Glide.with(itemView)
                    .load(productReview.urlVideo)
                    .placeholder(R.drawable.background_linear_light)
                    .into(itemView.image)
                itemView.textField?.text = productReview.name
                itemView.setOnClickListener {
                    startActivity(
                        Intent(activity, FullScreenVideoActivity::class.java)
                            .putExtra(VIDEO_URL_EXTRA, productReview.urlVideo)
                    )
                }
            }
        }
    }
}