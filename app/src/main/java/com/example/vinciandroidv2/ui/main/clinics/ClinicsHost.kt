package com.example.vinciandroidv2.ui.main.clinics

import android.os.Bundle
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.ui.Screens
import com.example.vinciandroidv2.ui.global.BaseFragment

class ClinicsHost : BaseFragment() {
    override val layoutRes = R.layout.host_clinics
    override val TAG = "ClinicsHost"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        replace(R.id.clinicsHost, Screens.Clinics.getClinicsFragment())
    }
}