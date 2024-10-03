package com.example.vinciandroidv2.ui.auth.questionnaire

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.inSpans
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.network.respons.AppState
import com.example.vinciandroidv2.network.respons.Procedure
import com.example.vinciandroidv2.network.respons.UserProcedureIdList
import com.example.vinciandroidv2.ui.Screens
import com.example.vinciandroidv2.ui.global.dp
import io.realm.RealmList
import kotlinx.android.synthetic.main.fragment_questionnaire_procedure_of_interest.*
import kotlinx.android.synthetic.main.fragment_sign_up_guestionnaire_base.*
import kotlinx.android.synthetic.main.item_row_drawable_text_view_sample_1.view.*
import kotlinx.android.synthetic.main.view_button_sample_one.view.textField
import kotlinx.android.synthetic.main.view_next_button.view.*

class ProcedureOfInterestFragment : QuestionnaireBaseFragment() {
    override val includedLayoutRes = R.layout.fragment_questionnaire_procedure_of_interest
    override val progress = 57
    override val TAG = "ProcedureOfInterestFragment"

    private val list = RealmHelper.realm?.where(Procedure::class.java)?.findAll() ?: RealmList()
    private var selectedList = ArrayList<Int>()

    private val adapter: Adapter by lazy { Adapter() }

    @SuppressLint("NotifyDataSetChanged")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        scipButton?.setOnClickListener {
            replace(android.R.id.content, Screens.Auth.getHairTypeFragment())
        }
        titleField?.updateLayoutParams<ConstraintLayout.LayoutParams> {
            verticalBias = 0.0f
            bottomToTop = ConstraintLayout.LayoutParams.UNSET
        }
        mainContainer?.updateLayoutParams<ConstraintLayout.LayoutParams> {
            topToBottom = titleField?.id ?: return@updateLayoutParams
            topMargin = 32.dp
            height = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
        }

        recyclerView?.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = this@ProcedureOfInterestFragment.adapter
        }

        if (QuestionnaireFlow.HOME == questionnaireFlow || QuestionnaireFlow.PROFILE == questionnaireFlow) {
            RealmHelper.getUserData()?.procedureList?.let { list ->
                selectedList.clear()
                list.forEach { item -> selectedList.add(item.id) }
                adapter.notifyDataSetChanged()
                updateNextButton(selectedList.isNotEmpty()) {
                    val user = UserProcedureIdList()
                    user.procedureIdList = selectedList
                    userPresenter.editUserProcedureIdList(user)
                }
            }
        }

//        updateBackButton(true) { activity?.onBackPressed() }
    }

    override fun setLocalization() {
        super.setLocalization()
        titleField?.text = RealmHelper.getLocalizedText("procedureofinterest.title.text")
        backButton?.text = SpannableStringBuilder().inSpans(UnderlineSpan()) {
            append(RealmHelper.getLocalizedText("procedureofinterest.button.back.text"))
        }
        includeNextButton?.nextButtonText?.text =
            RealmHelper.getLocalizedText("procedureofinterest.button.next.text")
    }

    inner class Adapter : RecyclerView.Adapter<Adapter.Holder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(
            LayoutInflater.from(context).inflate(
                R.layout.item_row_drawable_text_view_sample_1, parent, false
            )
        )

        override fun onBindViewHolder(holder: Holder, position: Int) =
            holder.bind(list[position])

        override fun getItemCount() = list.size

        inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            @SuppressLint("NotifyDataSetChanged")
            fun bind(procedure: Procedure) {
                Glide.with(itemView).load(procedure.urlImage).into(itemView.startDrawable)
                itemView.textField?.text = procedure.name ?: ""
                itemView.isSelected = selectedList.contains(procedure.id)
                itemView.setOnClickListener {
                    if (selectedList.contains(procedure.id)) selectedList.remove(procedure.id)
                    else selectedList.add(procedure.id)
                    notifyDataSetChanged()
                    updateNextButton(selectedList.isNotEmpty()) {
//                        RealmHelper.getUserData()?.let {
//                            val user = realm.copyFromRealm(it)
//                            user.procedureIdList = RealmList<Int>().apply {
//                                addAll(selectedList)
//                            }
//
//                            if (questionnaireFlow == QuestionnaireFlow.AUTH) {
//                                user.appState = AppState.HAIR_LOSS_TYPE_STATE.value
//                            }
//                        }
                        val user = UserProcedureIdList()
                        user.procedureIdList = selectedList
                        if (questionnaireFlow == QuestionnaireFlow.AUTH) {
                            user.appState = AppState.HAIR_LOSS_TYPE_STATE.value
                        }
                        userPresenter.editUserProcedureIdList(user)
                    }
                }
            }
        }
    }

    override fun userLoaded() {
        replace(android.R.id.content, Screens.Auth.getHairTypeFragment())
    }
}