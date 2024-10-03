package com.example.vinciandroidv2.ui.main.book

import android.annotation.SuppressLint
import android.content.Intent
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
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.network.respons.BookReview
import com.example.vinciandroidv2.ui.Screens
import com.example.vinciandroidv2.ui.global.BaseFragment
import com.example.vinciandroidv2.ui.global.dp
import com.example.vinciandroidv2.ui.global.getScreenWidth
import com.example.vinciandroidv2.ui.main.FullScreenVideoActivity
import com.example.vinciandroidv2.ui.main.VIDEO_URL_EXTRA
import kotlinx.android.synthetic.main.fragment_book_info_tab_overview.*
import kotlinx.android.synthetic.main.item_row_card_view_middle_one_text_view_play_button_sample_one.view.*
import kotlinx.android.synthetic.main.item_row_drawable_text_view_bottom_divider_sample_2.view.*
import kotlinx.android.synthetic.main.item_row_drawable_text_view_bottom_divider_sample_2.view.textField

class TabOverviewFragment : BaseFragment() {
    override val layoutRes = R.layout.fragment_book_info_tab_overview
    override val TAG = "TabOverviewFragment"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        titleTextField?.text = getBookInfo().name
        includeCardOne?.apply {
            setOnClickListener {
                replace(
                    R.id.content, Screens.Book.getExpandedPreArticleFragment(
                        getBookInfo().urlPreImage,
                        getBookInfo().preName,
                        getBookInfo().preContent,
                        getBookInfo().bookPreInstructionList,
                        getBookInfo().bookPreAdditionalList
                    )
                )
            }
        }
        includeCardTwo?.apply {
            divider?.isVisible = false
            setOnClickListener {
                replace(
                    R.id.content, Screens.Book.getExpandedPostArticleFragment(
                        getBookInfo().urlPostImage,
                        getBookInfo().postName,
                        getBookInfo().postContent,
                        getBookInfo().bookPostInstructionList,
                        getBookInfo().bookPostAdditionalList
                    )
                )
            }
        }
        mainTextField?.text = getBookInfo().about

        peopleSayingRecycler?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = ReviewAdapter(getBookInfo().bookReviewList ?: ArrayList())
        }
    }

    private fun getBookInfo() = (parentFragment as BookInfoFragment).bookInfo

    override fun setLocalization() {
        super.setLocalization()
        includeCardOne?.textField?.text =
            RealmHelper.getLocalizedText("home.bookinfo.tab.preoperation.text")
        includeCardTwo?.textField?.text =
            RealmHelper.getLocalizedText("home.bookinfo.tab.postoperation.text")
        whatPeopleAreSayingTextLabel2?.text =
            RealmHelper.getLocalizedText("home.bookinfo.tab.one.subtitle.text")
    }

    inner class ReviewAdapter(private var list: ArrayList<BookReview>) :
        RecyclerView.Adapter<ReviewAdapter.Holder>() {
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

        override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(list[position])

        override fun getItemCount() = list.size

        inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            @SuppressLint("NotifyDataSetChanged")
            fun bind(bookReview: BookReview) {
                Glide.with(itemView)
                    .load(bookReview.urlVideo)
                    .placeholder(R.drawable.background_linear_light)
                    .into(itemView.image)
                itemView.textField?.text = bookReview.name
                itemView.setOnClickListener {
                    startActivity(
                        Intent(activity, FullScreenVideoActivity::class.java)
                            .putExtra(VIDEO_URL_EXTRA, bookReview.urlVideo)
                    )
                }
            }
        }
    }
}