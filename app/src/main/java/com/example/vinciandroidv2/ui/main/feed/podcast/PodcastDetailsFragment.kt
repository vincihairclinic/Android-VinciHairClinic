package com.example.vinciandroidv2.ui.main.feed.podcast

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Html
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
import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.network.respons.BaseArticle
import com.example.vinciandroidv2.network.respons.Podcast
import com.example.vinciandroidv2.network.respons.PodcastEpisode
import com.example.vinciandroidv2.ui.Screens
import com.example.vinciandroidv2.ui.global.changeFormat
import com.example.vinciandroidv2.ui.global.podcastEpisodeInProgress
import com.example.vinciandroidv2.ui.main.MainActivity
import com.example.vinciandroidv2.ui.main.feed.BaseFeedFragment
import kotlinx.android.synthetic.main.fragment_podcast_details.*
import kotlinx.android.synthetic.main.item_podcast.view.*

class PodcastDetailsFragment(var idCam: Int = 0) : BaseFeedFragment() {
    override val layoutRes: Int
        get() = R.layout.fragment_podcast_details
    override val TAG: String
        get() = "PodcastDetailsFragment"
    private val podcastsAdapter: PodcastAdapter by lazy { PodcastAdapter() }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        with(recyclerView) {
            this?.layoutManager = LinearLayoutManager(context)
            this?.setHasFixedSize(true)
            this?.adapter = this@PodcastDetailsFragment.podcastsAdapter
        }
        articlePresenter.getPodcastByID(idCam)
        backButton?.setOnClickListener {
            activity?.onBackPressed()
        }
        textEpisods?.text = RealmHelper.getLocalizedText( "feed.main.podcast.other_episodes.text")
    }

    var podcast: Podcast? = null
    override fun articleListByCategoryLoaded(baseArticle: BaseArticle) {
        namePoscasts?.text = baseArticle.podcast?.name
        podcast = baseArticle.podcast
        val textHtml = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(baseArticle.podcast?.content ?: "", Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(baseArticle.podcast?.content ?: "")
        }
        textPoscasts?.text = textHtml
        Glide.with(this@PodcastDetailsFragment).load(baseArticle.podcast?.urlImage).into(imagePodcast)
        baseArticle.podcast?.podcastsEpisodes?.let { podcastsAdapter.addPodcasts(it) }
    }

    fun showAudioPlayer(item: PodcastEpisode, int: Int) {
        Handler().postDelayed({
            (activity as? MainActivity)?.showAndPlayAudio(Podcast(), podcastEpisode = item ?: PodcastEpisode(), isPodscat = false)
        }, 500)
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
            fun bind(item: PodcastEpisode?) {
                itemView.namePodcast.text = item?.name
                val textInfoSpanOne = SpannableStringBuilder()
                context?.let {
                    ResourcesCompat.getFont(it, R.font.montserrat_medium)?.style?.let { style ->
                        textInfoSpanOne.inSpans(
                            StyleSpan(style),
                            ForegroundColorSpan(Color.parseColor("#7D5F2B"))
                        ) { append("${item?.durationMin.toString()} min ") }
                    }
                }
                context?.let {
                    ResourcesCompat.getFont(it, R.font.montserrat_regular)?.style?.let { style ->
                        textInfoSpanOne.inSpans(
                            StyleSpan(style),
                            ForegroundColorSpan(Color.parseColor("#0E1012"))
                        ) { append("${item?.createdAt?.changeFormat("MMM dd")}") }
                    }
                }
                itemView.infoPlay.text = textInfoSpanOne
                Glide.with(this@PodcastDetailsFragment).load(item?.urlImage).into(itemView.imagePodcast)
                itemView.infoPlay?.setOnClickListener {
                    podcastEpisodeInProgress = item ?: PodcastEpisode()
                    (activity as? MainActivity)?.showAndPlayAudio(Podcast(), podcastEpisode = item ?: PodcastEpisode(), isPodscat = false)
                }
                itemView.setOnClickListener {
                }
            }
        }
    }
}