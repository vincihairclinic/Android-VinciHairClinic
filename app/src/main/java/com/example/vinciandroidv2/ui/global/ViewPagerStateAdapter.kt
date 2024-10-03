package com.example.vinciandroidv2.ui.global

import android.widget.Adapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.rd.PageIndicatorView

/** view pager with fragments **/

class ViewPagerStateAdapter(
    hostFragment: Fragment,
    private val fragmentList: ArrayList<Fragment>
) :
    FragmentStateAdapter(hostFragment) {
    override fun getItemCount() = fragmentList.count()
    override fun createFragment(position: Int) = fragmentList[position]
}

fun ViewPagerStateAdapter.initViewPager(
    viewPager: ViewPager2?,
    onPageChangeCallback: ViewPager2.OnPageChangeCallback?
) {
    viewPager?.adapter = this
    viewPager?.offscreenPageLimit = this.itemCount
    if (null != onPageChangeCallback) {
        viewPager?.registerOnPageChangeCallback(onPageChangeCallback)
    }
}

fun ViewPagerStateAdapter.initViewPagerWithPageIndicator(
    viewPager: ViewPager2?,
    pageIndicatorView: PageIndicatorView?,
    onPageChangeCallback: ((pageSelected: Int) -> Unit)?
) {
    viewPager?.adapter = this
    viewPager?.offscreenPageLimit = this.itemCount

    val count = this.itemCount
    if (count > 1) pageIndicatorView?.count = count
    else pageIndicatorView?.isVisible = false

    viewPager?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            pageIndicatorView?.setSelected(position)
            onPageChangeCallback?.invoke(position)
        }
    })
}


/** view pager with fragments and tabs **/

class ViewPagerWithTitleStateAdapter(
    hostFragment: Fragment,
    internal val titleList: Array<String>,
    private val fragmentList: Array<Fragment>
) :
    FragmentStateAdapter(hostFragment) {
    override fun getItemCount() = fragmentList.count()
    override fun createFragment(position: Int) = fragmentList[position]
}

fun ViewPagerWithTitleStateAdapter.initViewPagerWithTabs(
    viewPager: ViewPager2?,
    tabLayout: TabLayout?,
    onPageChangeCallback: ViewPager2.OnPageChangeCallback?
) {
    viewPager?.adapter = this
    viewPager?.offscreenPageLimit = this.itemCount
    if (null != onPageChangeCallback) {
        viewPager?.registerOnPageChangeCallback(onPageChangeCallback)
    }

    if (null != tabLayout && null != viewPager) {
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.id = position
            tab.text = titleList[position]
        }.attach()
    }
}


/** view pager with views (recyclerView) **/

fun RecyclerView.Adapter<out RecyclerView.ViewHolder>.initViewPagerWithPageIndicator(
    viewPager: ViewPager2?,
    pageIndicatorView: PageIndicatorView?,
    onPageChangeCallback: ((pageSelected: Int) -> Unit)?
) {
    viewPager?.adapter = this

    val count = this.itemCount
    if (count > 1) pageIndicatorView?.count = count
    else pageIndicatorView?.isVisible = false

    viewPager?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            pageIndicatorView?.setSelected(position)
            onPageChangeCallback?.invoke(position)
        }
    })
}