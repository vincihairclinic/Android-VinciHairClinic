package com.example.vinciandroidv2.ui.main.feed

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.network.respons.Article
import com.example.vinciandroidv2.network.respons.BaseArticle
import com.example.vinciandroidv2.ui.Screens
import com.example.vinciandroidv2.ui.global.changeFormat
import com.example.vinciandroidv2.ui.main.feed.podcast.PodcastsFragment
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_feed.*

class FeedFragment : BaseFeedFragment() {
    override val layoutRes = R.layout.fragment_feed
    override val TAG = "FeedFragment"

    private var tabViewPager: ViewPagerWithTitleStateAdapter? = null
    private var onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            (tabViewPager?.getFragment(position) as? FeedTabFragment)?.updateTabData()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    @SuppressLint("NotifyDataSetChanged")
    internal fun updateFeedData() {
        articlePresenter.getArticleCategoryList()
    }

    override fun articleCategoryListLoaded(baseArticle: BaseArticle) {
        if (null == tabViewPager) {
            tabViewPager = ViewPagerWithTitleStateAdapter(this)
            tabViewPagerContainer?.adapter = tabViewPager
            tabViewPagerContainer?.offscreenPageLimit =
                baseArticle.articleCategoryList?.size ?: ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
            tabViewPagerContainer?.registerOnPageChangeCallback(onPageChangeCallback)
        }

        tabViewPager?.apply {
            addOrUpdateFragment(1, "All" ?: "", FeedTabFragment())
            addOrUpdateFragment(2, RealmHelper.getLocalizedText( "feed.main.tabs.item.articles.text") ?: "", FeedTabFragment())
            addOrUpdateFragment(3, RealmHelper.getLocalizedText( "feed.main.tabs.item.podcasts.text") ?: "", PodcastsFragment())
            addOrUpdateFragment(4, RealmHelper.getLocalizedText( "feed.main.tabs.item.youtube.text") ?: "", YouTubeFragment())
            if (null != tabLayout && null != tabViewPagerContainer) {
                TabLayoutMediator(tabLayout, tabViewPagerContainer) { tab, position ->
                    tab.text = tabViewPager?.getTitle(position)
                }.attach()
            }
        }
    }

    fun updateMainFirstItemView(article: Article?) {
        if (null == article) {
            app_bar_image?.setImageResource(0)
            dateTextField?.text = ""
            titleTextField?.text = ""
            firstItemConstraint?.setOnClickListener(null)
        } else {
            Glide.with(this).load(article.urlImage).into(app_bar_image)
            dateTextField?.text = article.createdAt?.changeFormat("dd MMMM, yyyy")
            titleTextField?.text = article.name
            firstItemConstraint?.setOnClickListener {
                replace(
                    R.id.content,
                    Screens.Feed.getExpandedFeedArticleFragment(article.id)
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tabViewPagerContainer?.unregisterOnPageChangeCallback(onPageChangeCallback)
    }
}

class ViewPagerWithTitleStateAdapter(hostFragment: Fragment) : FragmentStateAdapter(hostFragment) {
    private val itemId = ArrayList<Long>()
    private val titleList = ArrayList<String>()
    private val fragmentList = ArrayList<Fragment>()

    fun addOrUpdateFragment(id: Long, title: String, fragment: Fragment) {
        if (itemId.contains(id)) {
            titleList[itemId.indexOf(id)] = title
        } else {
            itemId.add(id)
            titleList.add(title)
            fragmentList.add(fragment)
        }
    }

    fun getFragment(position: Int) = fragmentList[position]

    fun getTitle(position: Int) = titleList[position]

    override fun getItemCount() = fragmentList.count()
    override fun getItemId(position: Int) = itemId[position]
    override fun createFragment(position: Int) = fragmentList[position]
}