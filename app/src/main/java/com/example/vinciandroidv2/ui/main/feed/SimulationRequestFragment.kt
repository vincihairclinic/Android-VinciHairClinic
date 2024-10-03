package com.example.vinciandroidv2.ui.main.feed

import android.content.Intent
import android.os.Bundle
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.di.module.UserModule
import com.example.vinciandroidv2.network.respons.Simulation
import com.example.vinciandroidv2.ui.global.BaseFragment
import com.example.vinciandroidv2.ui.global.mvp.user.UserContract
import com.example.vinciandroidv2.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.fragment_simulation_request.bottomFinishButton
import kotlinx.android.synthetic.main.item_row_edit_text_sample_1.view.*
import toothpick.Scope
import toothpick.ktp.delegate.inject

class SimulationRequestFragment(var simulation: Simulation = Simulation()) : BaseFragment(), UserContract.View {
    override val layoutRes: Int
        get() = R.layout.fragment_simulation_request
    override val TAG: String
        get() = "SimulationRequestFragment"
    private val userPresenter: UserContract.Presenter by inject()
    override fun installModules(scope: Scope) {
        scope.installModules(UserModule(this))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        userPresenter.simulationRequestsCreate(simulation)

//        findButton(R.id.bottomFinishButton)?.apply {
//            setText("Close", R.color.primary_brown_7D5F2B, R.font.montserrat_semi_bold)
//            setBackgroundColor(R.color.background_white_FFFFFF)
//            onClick {
//                activity?.finish()
//                startActivity(Intent(activity, MainActivity::class.java))
//            }
//        }
        bottomFinishButton?.setOnClickListener {  activity?.finish()
            startActivity(Intent(activity, MainActivity::class.java))


        }

    }

}