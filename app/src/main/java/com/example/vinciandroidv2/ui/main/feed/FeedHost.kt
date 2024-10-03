package com.example.vinciandroidv2.ui.main.feed

import android.os.Bundle
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.ui.Screens
import com.example.vinciandroidv2.ui.global.BaseFragment

class FeedHost : BaseFragment() {
    override val layoutRes = R.layout.host_feed
    override val TAG = "FeedHost"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        replace(R.id.feedHost, Screens.Feed.getFeedFragment())
    }
}