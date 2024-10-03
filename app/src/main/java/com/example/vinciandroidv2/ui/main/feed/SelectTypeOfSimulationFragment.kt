package com.example.vinciandroidv2.ui.main.feed

import android.os.Bundle
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.helper.RealmHelper.realm
import com.example.vinciandroidv2.network.respons.SimulationRequestType
import com.example.vinciandroidv2.ui.Screens
import com.example.vinciandroidv2.ui.global.BaseFragment
import com.example.vinciandroidv2.ui.global.dp
import kotlinx.android.synthetic.main.fragment_select_type_of_simulation.*

class SelectTypeOfSimulationFragment : BaseFragment() {
    override val layoutRes: Int
        get() = R.layout.fragment_select_type_of_simulation
    override val TAG: String
        get() = "SelectTypeOfSimulationFragment"
    private var idSimulatorType: Int = 0
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        backButton?.setOnClickListener { activity?.onBackPressed() }
        backButtonText?.setOnClickListener { activity?.onBackPressed() }
        nextButtonText?.setOnClickListener {
            if (idSimulatorType != 0) {
                replace(
                    android.R.id.content,
                    Screens.Feed.getMspUploadPhotoFragment(idSimulatorType)
                )
            }
        }


        secondType?.setOnClickListener {
            secondType.setStrokeColor(resources.getColor(R.color.primary_brown_7D5F2B))
            secondType.strokeWidth = 1.dp
            firstType.setStrokeColor(resources.getColor(R.color.color_transparent))
            firstType.strokeWidth = 0.dp
            cardContainer.isSelected = true
            idSimulatorType = realm?.where(SimulationRequestType::class.java)?.findAll()?.firstOrNull()?.id ?: 0
        }
        firstType?.setOnClickListener {
            secondType.setStrokeColor(resources.getColor(R.color.color_transparent))
            secondType.strokeWidth = 0.dp
            firstType.setStrokeColor(resources.getColor(R.color.primary_brown_7D5F2B))
            firstType.strokeWidth = 1.dp
            cardContainer.isSelected = true
            idSimulatorType = realm?.where(SimulationRequestType::class.java)?.findAll()?.lastOrNull()?.id ?: 0
        }
    }


}
