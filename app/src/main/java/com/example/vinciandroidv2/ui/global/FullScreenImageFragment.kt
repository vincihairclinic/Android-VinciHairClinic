package com.example.vinciandroidv2.ui.global

import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.ui.Screens
import kotlinx.android.synthetic.main.fragment_full_screen_image.*

class FullScreenImageFragment(var urlImage: ArrayList<String>? = ArrayList()) : BaseFragment() {
    override val layoutRes = R.layout.fragment_full_screen_image
    override val TAG = "FullScreenImageFragment"
    private var adapter: ViewPagerAdapter? = null


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        backButton?.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
        adapter = ViewPagerAdapter(childFragmentManager)

//        allImages?.clear()
//            it.urlVideos?.let { it1 -> allImages?.addAll(it1) }
//            it.urlImages?.let { it1 -> allImages?.addAll(it1) }
        pageIndicatorView?.count = urlImage?.size ?: 0
        urlImage?.forEachIndexed { index, image ->
            adapter?.addFragment(Screens.getImageListScreen().apply {
                this.image = image
                this.urlImages?.addAll(urlImage ?: ArrayList())
            }, "")
        }
        viewPager?.adapter = adapter


    }
}
