package com.example.vinciandroidv2.ui.main.home

import android.os.Bundle
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.ui.Screens
import com.example.vinciandroidv2.ui.global.BaseFragment

class HomeHost : BaseFragment() {
    override val layoutRes = R.layout.host_home
    override val TAG = "HomeHost"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        replace(R.id.homeHost, Screens.Home.getHomeFragment())
    }
}