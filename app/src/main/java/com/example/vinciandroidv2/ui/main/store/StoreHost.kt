package com.example.vinciandroidv2.ui.main.store

import android.os.Bundle
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.ui.Screens
import com.example.vinciandroidv2.ui.global.BaseFragment

class StoreHost : BaseFragment() {
    override val layoutRes = R.layout.host_store
    override val TAG = "StoreHost"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        replace(R.id.storeHost, Screens.Store.getStoreFragment())
    }
}