package com.example.vinciandroidv2.ui.main.feed

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.network.respons.Podcast
import com.example.vinciandroidv2.network.respons.Youtube
import com.example.vinciandroidv2.ui.Screens
import jdroidcoder.ua.paginationrecyclerview.OnPageChangeListener
import kotlinx.android.synthetic.main.fragment_feed_tab.recyclerViewContainer
import kotlinx.android.synthetic.main.item_youtube.view.imageYoutube
import kotlinx.android.synthetic.main.item_youtube.view.infoPlay
import kotlinx.android.synthetic.main.item_youtube.view.nameYoutube

class YouTubeFragment : BaseFeedFragment(), OnPageChangeListener {
    override val layoutRes = R.layout.fragment_feed_tab
    override val TAG = "YouTubeFragment"
    private var page = 1
    private val youtubeAdapter: YouTubeAdapter by lazy { YouTubeAdapter() }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        with(recyclerViewContainer) {
            this?.layoutManager = LinearLayoutManager(context)
            this?.setHasFixedSize(true)
            this?.setOnPageChangeListener(this@YouTubeFragment)
            this?.adapter = this@YouTubeFragment.youtubeAdapter
        }
        articlePresenter.getYoutubeList(this.page)
    }

    override fun showVideos(videos: ArrayList<Youtube>) {
        youtubeAdapter.addYoutube(videos)
    }

//    override fun articleListByCategoryLoaded(baseArticle: BaseArticle) {
//        baseArticle.podcasts?.let { podcastsAdapter.addPodcasts(it) }
//    }

    override fun onPageChange(page: Int) {
        this.page = page + 1
        articlePresenter.getYoutubeList(this.page)
    }

    inner class YouTubeAdapter : RecyclerView.Adapter<YouTubeAdapter.Holder>() {
        private val youtube = ArrayList<Youtube>()

        @SuppressLint("NotifyDataSetChanged")
        fun addYoutube(funds: ArrayList<Youtube>) {
            funds.forEach { it ->
                if (null == this.youtube.firstOrNull { p -> p.id == it.id }) {
                    this.youtube.add(it)
                }
            }
            notifyDataSetChanged()
        }

        fun clear() {
            youtube.clear()
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(
            LayoutInflater.from(context)
                .inflate(R.layout.item_youtube, parent, false)
        )

        override fun getItemCount(): Int = youtube.count()

        override fun onBindViewHolder(holder: Holder, position: Int) {
            holder.bind(youtube[position])
        }

        inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            @SuppressLint("SetTextI18n")
            fun bind(item: Youtube?) {
                itemView.nameYoutube.text = item?.title
                itemView.infoPlay.text = item?.publishedAt
                context?.let {
                    Glide.with(it)
                        .load(item?.urlPreview)
                        .into(itemView.imageYoutube)
                }
                itemView.setOnClickListener {
                    try {
                        val browserIntent =
                            Intent(Intent.ACTION_VIEW, Uri.parse("${item?.urlSource}"))
                        startActivity(browserIntent)
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }
            }
        }
    }

}