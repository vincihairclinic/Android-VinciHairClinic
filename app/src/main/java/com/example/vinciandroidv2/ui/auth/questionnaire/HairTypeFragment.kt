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
import com.example.vinciandroidv2.helper.RealmHelper.copyFromRealm
import com.example.vinciandroidv2.network.respons.HairType
import com.example.vinciandroidv2.ui.Screens
import com.example.vinciandroidv2.ui.global.dp
import io.realm.RealmList
import kotlinx.android.synthetic.main.fragment_questionnaire_procedure_of_interest.*
import kotlinx.android.synthetic.main.fragment_sign_up_guestionnaire_base.*
import kotlinx.android.synthetic.main.item_row_drawable_text_view_sample_1.view.*
import kotlinx.android.synthetic.main.view_next_button.view.*

class HairTypeFragment: QuestionnaireBaseFragment() {
    override val TAG: String
        get() = "HairTypeFragmentHairTypeFragment"

    override val includedLayoutRes = R.layout.fragment_questionnaire_procedure_of_interest
    override val progress = 57

    private val list = RealmHelper.realm?.where(HairType::class.java)?.findAll() ?: RealmList()
    private var selectedItem: HairType? = null

    private val adapter: Adapter by lazy { Adapter() }

    @SuppressLint("NotifyDataSetChanged")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        scipButton?.setOnClickListener {
            replace(android.R.id.content, Screens.Auth.getHairLossTypeFragment())
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
            adapter = this@HairTypeFragment.adapter
        }

        if (QuestionnaireFlow.HOME == questionnaireFlow || QuestionnaireFlow.PROFILE == questionnaireFlow) {
            selectedItem = RealmHelper.getUserData()?.hairType
            adapter.notifyDataSetChanged()
            updateNextButton(selectedItem != null) {
                RealmHelper.getUserData()?.copyFromRealm()?.let {
                    it.hairTypeId = selectedItem?.id
                    userPresenter.editUser(it)
                }
            }
        }

//        updateBackButton(true) { activity?.onBackPressed() }
    }

    override fun setLocalization() {
        super.setLocalization()
        titleField?.text = RealmHelper.getLocalizedText("hairtype.title.text")
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
            fun bind(procedure: HairType) {
                Glide.with(itemView).load(procedure.urlImage).into(itemView.startDrawable)
                itemView.textField?.text = procedure.name ?: ""
                itemView.isSelected = selectedItem?.id == procedure.id
                itemView.setOnClickListener {
                    selectedItem = procedure
                    notifyDataSetChanged()
                    updateNextButton(selectedItem != null) {
                        RealmHelper.getUserData()?.copyFromRealm()?.let {
                            it.hairTypeId = selectedItem?.id
                            userPresenter.editUser(it)
                        }
                    }
                }
            }
        }
    }

    override fun userLoaded() {
        replace(android.R.id.content, Screens.Auth.getHairLossTypeFragment())
    }
}