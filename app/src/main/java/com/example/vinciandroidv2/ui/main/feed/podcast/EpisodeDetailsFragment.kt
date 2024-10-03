package com.example.vinciandroidv2.ui.main.feed.podcast

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
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
import android.widget.ProgressBar
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.inSpans
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.network.respons.Podcast
import com.example.vinciandroidv2.network.respons.PodcastEpisode
import com.example.vinciandroidv2.ui.global.*
import com.example.vinciandroidv2.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_episode_details.*
import kotlinx.android.synthetic.main.fragment_episode_details.imagePodcast
import kotlinx.android.synthetic.main.fragment_episode_details.namePoscasts
import kotlinx.android.synthetic.main.fragment_episode_details.playAudio
import kotlinx.android.synthetic.main.fragment_episode_details.progressBar
import kotlinx.android.synthetic.main.fragment_episode_details.recyclerView
import kotlinx.android.synthetic.main.fragment_episode_details.textPoscasts
import kotlinx.android.synthetic.main.item_podcast.view.*

class EpisodeDetailsFragment(var podcastEpisode: PodcastEpisode = PodcastEpisode()) : BaseFragment() {
    override val layoutRes: Int
        get() = R.layout.fragment_episode_details
    override val TAG: String
        get() = "EpisodeDetailsFragment"
    private val podcastsAdapter: EpisodeDetailsFragment.PodcastAdapter by lazy { PodcastAdapter() }
    var podcastEpisodeList: ArrayList<PodcastEpisode>? = ArrayList()
    var podcastEpisodeListShow: ArrayList<PodcastEpisode>? = ArrayList()
    var podcastsFragment: PodcastsFragment? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        showInfo(podcastEpisode)
        backButton?.setOnClickListener {
            activity?.onBackPressed()
            if (isPlay) {
                mediaPlayer?.stop()
                podcastsFragment?.showAudioPlayer()
            }
        }
        with(recyclerView) {
            this?.layoutManager = LinearLayoutManager(context)
            this?.setHasFixedSize(true)
            this?.adapter = this@EpisodeDetailsFragment.podcastsAdapter
        }
        textEpisods?.text = RealmHelper.getLocalizedText( "feed.main.podcast.other_episodes.text")
    }

    private var isPlay: Boolean = false
    private var podcastEpisodeSend = PodcastEpisode()

    @SuppressLint("SetTextI18n")
    fun showInfo(podcastEpisode: PodcastEpisode) {
        podcastEpisodeList?.let { podcastEpisodeListShow?.addAll(it) }
        podcastEpisodeListShow?.remove(podcastEpisode)
        podcastEpisodeSend = podcastEpisode
        namePoscasts?.text = podcastEpisode.name
        val textHtml = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(podcastEpisode.content ?: "", Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(podcastEpisode.content ?: "")
        }
        textPoscasts?.text = textHtml
        Glide.with(this@EpisodeDetailsFragment).load(podcastEpisode.urlImage).into(imagePodcast)

        podcastEpisodeListShow?.let { podcastsAdapter.addPodcasts(it) }

        val textInfoSpanOne = SpannableStringBuilder()
        context?.let {
            ResourcesCompat.getFont(it, R.font.montserrat_medium)?.style?.let { style ->
                textInfoSpanOne.inSpans(
                    StyleSpan(style),
                    ForegroundColorSpan(Color.parseColor("#7D5F2B"))
                ) { append("${podcastEpisode.durationMin} min ") }
            }
        }
        context?.let {
            ResourcesCompat.getFont(it, R.font.montserrat_regular)?.style?.let { style ->
                textInfoSpanOne.inSpans(
                    StyleSpan(style),
                    ForegroundColorSpan(Color.parseColor("#0E1012"))
                ) { append("${podcastEpisode.createdAt?.changeFormat("MMM dd")}") }
            }
        }
        infoPlay?.text = textInfoSpanOne
        if (podcastEpisode.id == podcastEpisodeInProgress.id) {
            showAndPlayAudio(podcastEpisode)
            isPlay = true
            podcastEpisodeInProgress = podcastEpisodeSend
            playAudio?.setImageResource(R.drawable.ic_pausa_small)
            progresInfo?.visibility = View.VISIBLE
        }

        try {
            if (!showPlayer) {
                (activity as? MainActivity)?.stopPlayer()
                (activity as? MainActivity)?.showAndPlayAudio(Podcast(), podcastEpisode = podcastEpisodeInProgress ?: PodcastEpisode(), isPodscat = false)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        playAudio?.setOnClickListener {
            if (isPlay) {
                isPlay = false
                (activity as? MainActivity)?.startStop(false)
                playAudio?.setImageResource(R.drawable.ic_play_smal)
                progresInfo?.visibility = View.GONE
            } else {
                isPlay = true
                (activity as? MainActivity)?.startStop(true)
                podcastEpisodeInProgress = podcastEpisodeSend
                playAudio?.setImageResource(R.drawable.ic_pausa_small)
                progresInfo?.visibility = View.VISIBLE
                showAndPlayAudio(podcastEpisode)
            }
        }

    }

    internal fun showAndPlayAudio(podcastEpisode: PodcastEpisode) {

        if (mediaPlayer != null) {
            mediaPlayer?.reset()
            mediaPlayer?.release()
            mediaPlayer = null
        }
        mediaPlayer = MediaPlayer()


        val fileSet = podcastEpisode.urlFile
        try {
            val myUri = Uri.parse(fileSet)
            mediaPlayer?.setDataSource(requireActivity(), myUri)
            mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
            mediaPlayer?.prepare()
            mediaPlayer?.setVolume(0f, 0f)
            if (currentProgress != 0) {
                mediaPlayer?.seekTo(currentProgress)
                progressBar.progress = currentProgress
            }
            mediaPlayer?.start()
            mediaPlayer?.setOnPreparedListener { mp ->
                progressBar.max = mp.duration
            }
            mediaPlayer?.let { update(it, progressBar, this@EpisodeDetailsFragment) }

        } catch (e: Exception) {

        }

        playAudio?.setOnClickListener {
            if (isPlay) {
                isPlay = false
                mediaPlayer?.pause()
                (activity as? MainActivity)?.startStop(true)
                playAudio?.setImageResource(R.drawable.ic_play_smal)
                progresInfo?.isVisible = false
            } else {
                isPlay = true
                mediaPlayer?.start()
                playAudio?.setImageResource(R.drawable.ic_pausa_small)
                progresInfo?.isVisible = true
                if (isPlayCheck) {
                    (activity as? MainActivity)?.showAndPlayAudio(Podcast(), podcastEpisode = podcastEpisodeInProgress ?: PodcastEpisode(), isPodscat = false)
                } else {
                    (activity as? MainActivity)?.startStop(false)
                }
            }
        }

    }

    private fun update(
        mediaPlayer: MediaPlayer,
        progressBar: ProgressBar,
        context: EpisodeDetailsFragment
    ) {
        activity?.runOnUiThread {
            progressBar.progress = mediaPlayer.currentPosition
            currentProgress = mediaPlayer.currentPosition
            val handler = Handler()
            try {
                val runnable = Runnable {
                    try {
                        if (mediaPlayer.currentPosition > -1) {
                            try {
                                update(mediaPlayer, progressBar, context)
                            } catch (e: java.lang.Exception) {
                                e.printStackTrace()
                            }
                        }
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }
                handler.postDelayed(runnable, 500)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    inner class PodcastAdapter : RecyclerView.Adapter<PodcastAdapter.Holder>() {
        private val podcasts = ArrayList<PodcastEpisode>()

        @SuppressLint("NotifyDataSetChanged")
        fun addPodcasts(funds: ArrayList<PodcastEpisode>) {
            this.podcasts.clear()
            funds.forEach { it ->
                if (null == this.podcasts.firstOrNull { p -> p.id == it.id }) {
                    this.podcasts.add(it)
                }
            }

            otherEpisodesContainer?.isVisible = this.podcasts.isNotEmpty()
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
                Glide.with(this@EpisodeDetailsFragment).load(item?.urlImage).into(itemView.imagePodcast)
                itemView.setOnClickListener {
                    showPlayer = false
                    isPlay = false
                    podcastEpisodeSend = item ?: PodcastEpisode()
                    podcastEpisodeInProgress = item ?: PodcastEpisode()
                    item?.let { it1 -> showInfo(it1) }
                }

            }
        }
    }
}