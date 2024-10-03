package com.example.vinciandroidv2.ui.main.feed

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.network.respons.Article
import com.example.vinciandroidv2.network.respons.ArticleType
import com.example.vinciandroidv2.network.respons.BaseArticle
import com.example.vinciandroidv2.ui.Screens
import com.example.vinciandroidv2.ui.global.dp
import com.example.vinciandroidv2.ui.global.facebookLink
import com.example.vinciandroidv2.ui.global.instagramLink
import com.example.vinciandroidv2.ui.global.twitterLink
import com.google.android.flexbox.*
import com.makeramen.roundedimageview.RoundedImageView
import jdroidcoder.ua.paginationrecyclerview.OnPageChangeListener
import kotlinx.android.synthetic.main.fragment_feed_tab.*

class FeedTabFragment(id: Int =1) : BaseFeedFragment(), OnPageChangeListener {
    override val layoutRes = R.layout.fragment_feed_tab
    override val TAG = "FeedTabFragment"

    private val adapter: Adapter by lazy { Adapter() }
    private var page = 0
    private var list = ArrayList<Article>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recyclerViewContainer?.apply {
            layoutManager = FlexboxLayoutManager(context).apply {
                flexDirection = FlexDirection.ROW
                flexWrap = FlexWrap.WRAP
            }
            setHasFixedSize(true)
            adapter = this@FeedTabFragment.adapter
            setOnPageChangeListener(this@FeedTabFragment)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    internal fun updateTabData() {
        updateMainFirstItemView(true)
        onPageChange(0)
    }

    override fun onPageChange(page: Int) {
        this.page = page + 1
        if (id != 1){
            articlePresenter.getArticleListByCategory( this.page)
        }else{
            articlePresenter.getPodcastList( this.page)
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun articleListByCategoryLoaded(baseArticle: BaseArticle) {
        if (1 == page) list.clear()
        list.addAll(baseArticle.articleList ?: ArrayList())

        if (1 == page) {
            updateMainFirstItemView()
            Article().apply { articleType = ArticleType.SUBSCRIBE_SOCIALS }.also {
                if (list.size > 6) list.add(6, it) else list.add(it)
            }
        }
        adapter.notifyDataSetChanged()
    }

    private fun updateMainFirstItemView(removeData: Boolean = false) {
        if (removeData) {
            (parentFragment as? FeedFragment)?.updateMainFirstItemView(null)
        } else {
            list.getOrNull(0)?.let { itemOne ->
                parentFragment
                (parentFragment as? FeedFragment)?.updateMainFirstItemView(itemOne)
            }
        }
    }

    inner class Adapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private val ARTICLE_SMALL_ITEM = 0
        private val ARTICLE_MIDDLE_ITEM = 1
        private val SUBSCRIBE_ITEM = 2

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            when (viewType) {
                ARTICLE_SMALL_ITEM -> ArticleSmallViewHolder(
                    LayoutInflater.from(context).inflate(
                        R.layout.item_row_card_view_small_one_text_view_sample_two,
                        parent,
                        false
                    )
                )
                ARTICLE_MIDDLE_ITEM -> ArticleMiddleViewHolder(
                    LayoutInflater.from(context).inflate(
                        R.layout.item_row_card_view_middle_one_text_view_sample_two,
                        parent,
                        false
                    )
                )
                SUBSCRIBE_ITEM -> SubscribeViewHolder(
                    LayoutInflater.from(context).inflate(
                        R.layout.view_subscribe_on_socials,
                        parent,
                        false
                    )
                )
                else -> SubscribeViewHolder(
                    LayoutInflater.from(context).inflate(
                        R.layout.view_subscribe_on_socials,
                        parent,
                        false
                    )
                )
            }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (getItemViewType(position)) {
                ARTICLE_SMALL_ITEM -> (holder as ArticleSmallViewHolder).bind(list[position.plus(1)])
                ARTICLE_MIDDLE_ITEM -> (holder as ArticleMiddleViewHolder).bind(list[position.plus(1)])
                SUBSCRIBE_ITEM -> (holder as SubscribeViewHolder).bind()
            }
        }

        override fun getItemCount() =
            if (1 == list.size) list.size
            else list.size.minus(1)

        override fun getItemViewType(position: Int) =
            when {
                1 == list.size -> SUBSCRIBE_ITEM
                list[position.plus(1)].articleType == ArticleType.SUBSCRIBE_SOCIALS -> SUBSCRIBE_ITEM
                0 == position || 1 == position -> {
                    if (3 == list.size) ARTICLE_MIDDLE_ITEM
                    else ARTICLE_SMALL_ITEM
                }
                else -> ARTICLE_MIDDLE_ITEM
            }

        inner class ArticleSmallViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(article: Article) {

                (itemView.layoutParams as? FlexboxLayoutManager.LayoutParams)?.flexGrow = 1f
                itemView.findViewById<RoundedImageView>(R.id.image)?.apply {
                    this.updateLayoutParams<ConstraintLayout.LayoutParams> {
                        width = ConstraintLayout.LayoutParams.MATCH_PARENT
                        if (0 == adapterPosition) marginStart = 20.dp
                        if (1 == adapterPosition) marginEnd = 20.dp
                    }
                    Glide.with(this).load(article.urlImage).into(this)
                }
                itemView.findViewById<TextView>(R.id.textField)?.text = article.name
                itemView.setOnClickListener {
                    replace(
                        R.id.content,
                        Screens.Feed.getExpandedFeedArticleFragment(article.id)
                    )
                }
            }
        }

        inner class ArticleMiddleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(article: Article) {
                itemView.findViewById<RoundedImageView>(R.id.image)?.apply {
                    Glide.with(this).load(article.urlImage).into(this)
                }
                itemView.findViewById<TextView>(R.id.textField)?.text = article.name
                itemView.setOnClickListener {
                    replace(
                        R.id.content,
                        Screens.Feed.getExpandedFeedArticleFragment(article.id)
                    )
                }
            }
        }

        inner class SubscribeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind() {
                itemView.findViewById<TextView>(R.id.titleTextField2)?.text =
                    RealmHelper.getLocalizedText("feed.main.card.subscribe.title.text")
                itemView.findViewById<CardView>(R.id.socialOneButton)?.setOnClickListener {
                    facebookLink?.let { link ->
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
                    }
                }
                itemView.findViewById<CardView>(R.id.socialTwoButton)?.setOnClickListener {
                    instagramLink?.let { link ->
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
                    }
                }
                itemView.findViewById<CardView>(R.id.socialThreeButton)?.setOnClickListener {
                    twitterLink?.let { link ->
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
                    }
                }
            }
        }
    }
}