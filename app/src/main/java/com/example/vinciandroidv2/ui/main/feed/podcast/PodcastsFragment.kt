package com.example.vinciandroidv2.ui.main.feed.podcast

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.inSpans
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.network.respons.BaseArticle
import com.example.vinciandroidv2.network.respons.Podcast
import com.example.vinciandroidv2.network.respons.PodcastEpisode
import com.example.vinciandroidv2.ui.Screens
import com.example.vinciandroidv2.ui.global.changeFormat
import com.example.vinciandroidv2.ui.global.podcastEpisodeInProgress
import com.example.vinciandroidv2.ui.global.showPlayer
import com.example.vinciandroidv2.ui.main.MainActivity
import com.example.vinciandroidv2.ui.main.feed.BaseFeedFragment
import jdroidcoder.ua.paginationrecyclerview.OnPageChangeListener
import kotlinx.android.synthetic.main.fragment_feed_tab.*
import kotlinx.android.synthetic.main.item_podcast.view.*

class PodcastsFragment : BaseFeedFragment(), OnPageChangeListener {
    override val layoutRes = R.layout.fragment_feed_tab
    override val TAG = "PodcastsFragment"
    private var page = 1
    private val podcastsAdapter: PodcastAdapter by lazy { PodcastAdapter() }
    private val podcasts: ArrayList<Podcast> = ArrayList()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        with(recyclerViewContainer) {
            this?.layoutManager = LinearLayoutManager(context)
            this?.setHasFixedSize(true)
            this?.setOnPageChangeListener(this@PodcastsFragment)
            this?.adapter = this@PodcastsFragment.podcastsAdapter
        }
        articlePresenter.getPodcastList(this.page)
    }

    fun showAudioPlayer() {
        Handler().postDelayed({
            (activity as? MainActivity)?.showPlayer()
        }, 500)
    }

    override fun articleListByCategoryLoaded(baseArticle: BaseArticle) {
        baseArticle.podcasts?.let { podcastsAdapter.addPodcasts(it) }

    }

    override fun onPageChange(page: Int) {
        this.page = page + 1
        articlePresenter.getPodcastList(this.page)
    }

    inner class PodcastAdapter : RecyclerView.Adapter<PodcastAdapter.Holder>() {
        private val podcasts = ArrayList<PodcastEpisode>()

        @SuppressLint("NotifyDataSetChanged")
        fun addPodcasts(funds: ArrayList<PodcastEpisode>) {
            funds.forEach { it ->
                if (null == this.podcasts.firstOrNull { p -> p.id == it.id }) {
                    this.podcasts.add(it)
                }
            }
            notifyDataSetChanged()
        }

        fun clear() {
            podcasts.clear()
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(
            LayoutInflater.from(context)
                .inflate(R.layout.item_podcast, parent, false)
        )

        override fun getItemCount(): Int = podcasts.count()

        override fun onBindViewHolder(holder: Holder, position: Int) {
            holder.bind(podcasts[position])
        }


        inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            @SuppressLint("SetTextI18n")
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

                Glide.with(this@PodcastsFragment).load(item.urlImage).into(itemView.imagePodcast)
                itemView.setOnClickListener {
                    (activity as? MainActivity)?.hidePlayer()
                    add(R.id.content, Screens.Feed.getEpisodeDetailsFragment(item ?: PodcastEpisode()).apply {
                        podcastEpisodeList?.addAll(podcasts)
                        this.podcastsFragment = this@PodcastsFragment
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