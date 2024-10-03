package com.example.vinciandroidv2.ui.main.feed

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.inSpans
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.network.respons.Article
import com.example.vinciandroidv2.network.respons.Podcast
import com.example.vinciandroidv2.network.respons.PodcastEpisode
import com.example.vinciandroidv2.ui.Screens
import com.example.vinciandroidv2.ui.global.changeFormat
import com.example.vinciandroidv2.ui.global.dp
import com.example.vinciandroidv2.ui.global.podcastEpisodeInProgress
import com.example.vinciandroidv2.ui.global.showPlayer
import com.example.vinciandroidv2.ui.main.MainActivity
import com.google.android.flexbox.FlexboxLayoutManager
import com.makeramen.roundedimageview.RoundedImageView
import kotlinx.android.synthetic.main.fragment_all_recommended.*
import kotlinx.android.synthetic.main.fragment_feed_tab.*
import kotlinx.android.synthetic.main.item_podcast.view.*

class AllRecommendedFragment : BaseFeedFragment() {
    override val layoutRes: Int
        get() = R.layout.fragment_all_recommended
    override val TAG: String
        get() = "AllRecommendedFragment"

    var recommendedPodcasts: ArrayList<PodcastEpisode> = ArrayList()
    var recommendedArticles: ArrayList<Article> = ArrayList()
    val adapter: Adapter by lazy { Adapter() }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        backButton?.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
        recyclerView?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = this@AllRecommendedFragment.adapter
        }
    }

    override fun setLocalization() {
        recommendedTitle?.text = RealmHelper.getLocalizedText("home.main.recommended.title.text")
    }

    inner class Adapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private val ARTICLE_ITEM = 0
        private val PODCAS_ITEM = 1

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            when (viewType) {
                ARTICLE_ITEM -> ArticleViewHolder(
                    LayoutInflater.from(context).inflate(
                        R.layout.item_row_card_view_middle_one_text_view_sample_two,
                        parent,
                        false
                    )
                )
                PODCAS_ITEM -> PodcastViewHolder(
                    LayoutInflater.from(context).inflate(
                        R.layout.item_podcast,
                        parent,
                        false
                    )
                )
                else -> {
                    PodcastViewHolder(
                        LayoutInflater.from(context).inflate(
                            R.layout.item_podcast,
                            parent,
                            false
                        )
                    )
                }
            }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (getItemViewType(position)) {
                ARTICLE_ITEM -> (holder as ArticleViewHolder).bind(recommendedArticles[position - recommendedPodcasts.size])
                PODCAS_ITEM -> (holder as PodcastViewHolder).bind(recommendedPodcasts[position])
            }
        }

        override fun getItemCount() = recommendedPodcasts.size + recommendedArticles.size

        override fun getItemViewType(position: Int): Int {
            if (position > recommendedPodcasts.size - 1)
                return ARTICLE_ITEM
            return PODCAS_ITEM
        }

        inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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

        inner class PodcastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(item: PodcastEpisode) {
                itemView.namePodcast.text = item.name
                val textInfoSpanOne = SpannableStringBuilder()
                context?.let {
                    ResourcesCompat.getFont(it, R.font.montserrat_medium)?.style?.let { style ->
                        textInfoSpanOne.inSpans(
                            StyleSpan(style),
                            ForegroundColorSpan(Color.parseColor("#7D5F2B"))
                        ) { append("${item.durationMin} min ") }
                    }
                }
                context?.let {
                    ResourcesCompat.getFont(it, R.font.montserrat_regular)?.style?.let { style ->
                        textInfoSpanOne.inSpans(
                            StyleSpan(style),
                            ForegroundColorSpan(Color.parseColor("#0E1012"))
                        ) { append("${item.createdAt?.changeFormat("MMM dd")}") }
                    }
                }
                itemView.infoPlay.text = textInfoSpanOne

                Glide.with(requireContext()).load(item.urlImage).into(itemView.imagePodcast)
                itemView.setOnClickListener {
                    (activity as? MainActivity)?.hidePlayer()
                    add(R.id.content, Screens.Feed.getEpisodeDetailsFragment(item ?: PodcastEpisode()).apply {
                        podcastEpisodeList?.addAll(recommendedPodcasts)
                    })
                }

                itemView.infoPlay?.setOnClickListener {
                    showPlayer = true
                    podcastEpisodeInProgress = item ?: PodcastEpisode()
                    (activity as? MainActivity)?.showAndPlayAudio(Podcast(), podcastEpisode = item ?: PodcastEpisode(), isPodscat = false)
                }
            }
        }
    }
}