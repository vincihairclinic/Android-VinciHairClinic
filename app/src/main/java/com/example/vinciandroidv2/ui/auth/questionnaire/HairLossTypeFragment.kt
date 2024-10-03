package com.example.vinciandroidv2.ui.auth.questionnaire

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.inSpans
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.helper.RealmHelper.copyFromRealm
import com.example.vinciandroidv2.network.respons.AppState
import com.example.vinciandroidv2.network.respons.HairLossType
import com.example.vinciandroidv2.ui.Screens
import com.example.vinciandroidv2.ui.global.dp
import com.example.vinciandroidv2.ui.main.MainActivity
import io.realm.RealmList
import kotlinx.android.synthetic.main.fragment_questionnaire_procedure_of_interest.*
import kotlinx.android.synthetic.main.fragment_sign_up_guestionnaire_base.*
import kotlinx.android.synthetic.main.item_row_drawable_card.view.drawable
import kotlinx.android.synthetic.main.view_button_sample_one.view.*
import kotlinx.android.synthetic.main.view_next_button.view.*

class HairLossTypeFragment : QuestionnaireBaseFragment() {
    override val includedLayoutRes = R.layout.fragment_questionnaire_hair_loss_type
    override val progress = 76
    override val TAG = "HairLossTypeFragment"

    private val list = RealmHelper.realm?.where(HairLossType::class.java)?.findAll()
        ?.filter { p -> p.genderId == RealmHelper.getUserData()?.genderId } ?: RealmList()
    private var selectedItemId = -1
    private val adapter: Adapter by lazy { Adapter() }

    @SuppressLint("NotifyDataSetChanged")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        scipButton?.setOnClickListener {
            if (QuestionnaireFlow.HOME == questionnaireFlow) {
                replace(android.R.id.content, Screens.Auth.getYourNumberFragment().apply {
                    isBook = true
                })
            } else {
                replace(android.R.id.content, Screens.Auth.getWelcomeFragment())
            }
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
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = this@HairLossTypeFragment.adapter
        }

        if (QuestionnaireFlow.HOME == questionnaireFlow) {
            selectedItemId = RealmHelper.getUserData()?.hairLossTypeId ?: -1
            adapter.notifyDataSetChanged()
            updateNextButton(-1 != selectedItemId) {
                RealmHelper.getUserData()?.copyFromRealm()?.let {
                    it.hairLossTypeId = selectedItemId
                    userPresenter.editUser(it)
                }
            }
        }

//        updateBackButton(true) { activity?.onBackPressed() }
    }

    override fun setLocalization() {
        super.setLocalization()
        titleField?.text = RealmHelper.getLocalizedText("hairloss.title.text")
        backButton?.text = SpannableStringBuilder().inSpans(UnderlineSpan()) {
            append(RealmHelper.getLocalizedText("hairloss.button.back.text"))
        }
        includeNextButton?.nextButtonText?.text =
            RealmHelper.getLocalizedText("hairloss.button.next.text")
    }

    inner class Adapter : RecyclerView.Adapter<Adapter.Holder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(
            LayoutInflater.from(context).inflate(
                R.layout.item_row_drawable_card, parent, false
            )
        )

        override fun onBindViewHolder(holder: Holder, position: Int) =
            holder.bind(list[position])

        override fun getItemCount() = list.size

        inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            @SuppressLint("NotifyDataSetChanged")
            fun bind(hairLossType: HairLossType) {
                Glide.with(itemView).load(hairLossType.urlImage).into(itemView.drawable)
                itemView.textField?.text = hairLossType.name ?: ""
                itemView.isSelected = selectedItemId == hairLossType.id
                itemView.setOnClickListener {
                    selectedItemId = hairLossType.id
                    notifyDataSetChanged()
                    updateNextButton(-1 != selectedItemId) {

                        RealmHelper.getUserData()?.copyFromRealm()?.let { user ->

                            user.hairLossTypeId = selectedItemId

                            if (questionnaireFlow == QuestionnaireFlow.AUTH) {
                                user.appState = AppState.CLINIC_STATE.value
                            }

                            userPresenter.editUser(user)
                        }
                    }
                }
            }
        }
    }

    override fun userLoaded() {
        if (questionnaireFlow == QuestionnaireFlow.HOME) {
            replace(android.R.id.content, Screens.Auth.getYourNumberFragment().apply {
                isBook = true
            })
        } else {
            replace(android.R.id.content, Screens.Auth.getWelcomeFragment())
        }

    }
}