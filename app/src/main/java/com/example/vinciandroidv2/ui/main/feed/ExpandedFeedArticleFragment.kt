package com.example.vinciandroidv2.ui.main.feed

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.network.respons.Article
import com.example.vinciandroidv2.network.respons.BaseArticle
import com.example.vinciandroidv2.ui.Screens
import com.example.vinciandroidv2.ui.global.changeFormat
import com.example.vinciandroidv2.ui.global.facebookLink
import com.example.vinciandroidv2.ui.global.instagramLink
import com.example.vinciandroidv2.ui.global.twitterLink
import jdroidcoder.ua.paginationrecyclerview.OnPageChangeListener
import kotlinx.android.synthetic.main.fragment_expanded_feed_article.*
import kotlinx.android.synthetic.main.item_row_card_view_small_one_text_view_sample_one.view.*

class ExpandedFeedArticleFragment(private val articleId: Int) : BaseFeedFragment(),
    OnPageChangeListener {
    override val layoutRes = R.layout.fragment_expanded_feed_article
    override val TAG = "ExpandedFeedArticleFragment"

    private val adapter: Adapter by lazy { Adapter() }
    private var page = 0
    private var nextList = ArrayList<Article>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        backButton?.setOnClickListener { activity?.onBackPressed() }
        articlePresenter.getArticleById(articleId)

        upNextRecycler?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = this@ExpandedFeedArticleFragment.adapter
            setOnPageChangeListener(this@ExpandedFeedArticleFragment)
        }
    }

    override fun articleByIdLoaded(baseArticle: BaseArticle) {
        baseArticle.article?.let { article ->
            image?.let { image ->
                Glide.with(image).load(article.urlImage).into(image)
            }
            dateTextField?.text = article.createdAt?.changeFormat("dd MMMM, yyyy")
            titleTextField?.text = article.name
            val textHtml = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(article.content ?: "", Html.FROM_HTML_MODE_COMPACT)
            } else {
                Html.fromHtml(article.content ?: "")
            }
            mainTextField?.text = textHtml

            shareButton?.setOnClickListener {

            }
            socialOneButton?.setOnClickListener {
                facebookLink?.let { link ->
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
                }
            }
            socialTwoButton?.setOnClickListener {
                instagramLink?.let { link ->
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
                }
            }
            socialThreeButton?.setOnClickListener {
                twitterLink?.let { link ->
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
                }
            }

            onPageChange(0)
        }
    }

    override fun onPageChange(page: Int) {
        this.page = page.plus(1)
        articlePresenter.getNextArticleListAfterArticleId(articleId, this.page)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun nextArticleListLoaded(baseArticle: BaseArticle) {
        if (1 == page && 0 != baseArticle.articleList?.size) {
            upNextLabel?.isVisible = true
            upNextRecycler?.isVisible = true
        }
        nextList.addAll(baseArticle.articleList ?: ArrayList())
        adapter.notifyDataSetChanged()
    }

    inner class Adapter : RecyclerView.Adapter<Adapter.Holder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(
            LayoutInflater.from(context).inflate(
                R.layout.item_row_card_view_small_one_text_view_sample_one, parent, false
            )
        )

        override fun onBindViewHolder(holder: Holder, position: Int) =
            holder.bind(nextList[position])

        override fun getItemCount() = nextList.size

        inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            @SuppressLint("NotifyDataSetChanged")
            fun bind(article: Article) {
                Glide.with(itemView).load(article.urlImage).into(itemView.image)
                itemView.textField?.text = article.name
                itemView.setOnClickListener {
                    replace(
                        R.id.content,
                        Screens.Feed.getExpandedFeedArticleFragment(article.id)
                    )
                }
            }
        }
    }

    override fun setLocalization() {
        super.setLocalization()
        titleTextField2?.text = StringBuilder()
            .append(RealmHelper.getLocalizedText("feed.expanded.article.card.share.title.text.one"))
            .append("\n")
            .append(RealmHelper.getLocalizedText("feed.expanded.article.card.share.title.text.two"))
        upNextLabel?.text = RealmHelper.getLocalizedText("feed.expanded.article.subtitle.text")
    }

    override fun startLoading() {
        super.startLoading()
    }

    override fun stopLoading() {
        super.stopLoading()
    }
}