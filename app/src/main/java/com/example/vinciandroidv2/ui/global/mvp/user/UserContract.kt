package com.example.vinciandroidv2.ui.global.mvp.user

import com.example.vinciandroidv2.network.respons.Simulation
import com.example.vinciandroidv2.network.respons.User
import com.example.vinciandroidv2.network.respons.UserProcedureIdList
import com.example.vinciandroidv2.ui.global.mvp.BaseView

interface UserContract {
    interface View : BaseView {
        fun userLoaded() {}
        fun userEdited() {}
        fun accountDeleted() {}
        fun simulationRequestsDone() {}
    }

    interface Presenter {
        fun getUser()
        fun editUser(user: User)
        fun editUserProcedureIdList(userProcedureIdList: UserProcedureIdList)
        fun deleteAccount()
        fun simulationRequestsCreate(simulation: Simulation)
    }
}