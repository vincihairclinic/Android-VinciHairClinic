package com.example.vinciandroidv2.ui.auth.questionnaire

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
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
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.di.module.UserModule
import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.helper.RealmHelper.copyFromRealm
import com.example.vinciandroidv2.network.respons.AppState
import com.example.vinciandroidv2.network.respons.Clinic
import com.example.vinciandroidv2.network.respons.Territory
import com.example.vinciandroidv2.ui.Screens
import com.example.vinciandroidv2.ui.global.BaseFragment
import com.example.vinciandroidv2.ui.global.dp
import com.example.vinciandroidv2.ui.global.mvp.user.UserContract
import com.google.android.material.tabs.TabLayout
import io.realm.RealmList
import kotlinx.android.synthetic.main.fragment_questionnaire_prefered_clinic.*
import kotlinx.android.synthetic.main.fragment_questionnaire_prefered_clinic.backButton
import kotlinx.android.synthetic.main.fragment_questionnaire_prefered_clinic.backButtonText
import kotlinx.android.synthetic.main.fragment_questionnaire_prefered_clinic.includeNextButton
import kotlinx.android.synthetic.main.fragment_questionnaire_prefered_clinic.scipButton
import kotlinx.android.synthetic.main.fragment_questionnaire_prefered_clinic.titleField
import kotlinx.android.synthetic.main.fragment_your_number.*
import kotlinx.android.synthetic.main.item_row_drawable_text_view_sample_2.view.*
import kotlinx.android.synthetic.main.view_button_sample_one.view.textField
import kotlinx.android.synthetic.main.view_next_button.view.*
import toothpick.Scope
import toothpick.ktp.delegate.inject

class PreferredClinicFragment : BaseFragment(), UserContract.View {

    override val layoutRes: Int
        get() = R.layout.fragment_questionnaire_prefered_clinic
    override val TAG: String
        get() = "PreferredClinicFragment"
    private val list = RealmHelper.realm?.where(Clinic::class.java)?.findAll() ?: RealmList()
    private var selectedItemId = -1
    private val userPresenter: UserContract.Presenter by inject()
    override fun installModules(scope: Scope) {
        scope.installModules(UserModule(this))
    }

    private val adapter: Adapter by lazy { Adapter() }

    @SuppressLint("NotifyDataSetChanged")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        backButton?.setOnClickListener {
            activity?.onBackPressed()
        }
        backButtonText.setOnClickListener {
            activity?.onBackPressed()
        }
        scipButton?.setOnClickListener {
            if (questionnaireFlow == QuestionnaireFlow.HOME) {
                replace(android.R.id.content, Screens.Auth.getHairPhotosFragment())
            } else {
                replace(android.R.id.content, Screens.Auth.getHairPhotosFragment())
            }
        }
        titleField?.updateLayoutParams<ConstraintLayout.LayoutParams> {
            verticalBias = 0.0f
            bottomToTop = ConstraintLayout.LayoutParams.UNSET
        }


        recyclerView?.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = this@PreferredClinicFragment.adapter
        }

        if (QuestionnaireFlow.HOME == questionnaireFlow) {
            selectedItemId = RealmHelper.getUserData()?.clinicId ?: -1
            adapter.notifyDataSetChanged()
            if (-1 != selectedItemId) {
                includeNextButton?.isSelected = true
            }
            includeNextButton?.setOnClickListener {
                RealmHelper.getUserData()?.copyFromRealm()?.let {
                    it.clinicId = selectedItemId
                    userPresenter.editUser(it)
                }
            }

        }
        initTabLayout()
//        updateBackButton(true) { activity?.onBackPressed() }
        mainContainer?.updateLayoutParams<ConstraintLayout.LayoutParams> {
            topToBottom = titleField?.id ?: return@updateLayoutParams
            topMargin = 32.dp
            height = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
        }
    }

    private fun initTabLayout() {
        tabLayout?.removeAllTabs()

        RealmHelper.realm?.where(Territory::class.java)?.findAll()?.forEach {
            tabLayout?.newTab()?.apply {
                id = it.id
                text = it.name
                Glide.with(this@PreferredClinicFragment)
                    .asBitmap()
                    .load(it.urlAreaImage)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {
                            icon = BitmapDrawable(resources, resource)
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {}
                    })
            }?.also { tabLayout?.addTab(it) }
        }
        tabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onTabSelected(tab: TabLayout.Tab?) {
                activity?.getColor(R.color.primary_brown_7D5F2B)?.let {
                    tab?.icon?.setTint(it)
                }
                RealmHelper.realm?.where(Clinic::class.java)
                    ?.findAll()
                    ?.filter { it.territoryId == tab?.id }
                    ?.filterNotNull()
                    ?.let {
                        adapter.updateList(arrayListOf<Clinic>().apply { addAll(it) })
                    }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                activity?.getColor(R.color.text_light_495057_65)?.let {
                    tab?.icon?.setTint(it)
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                onTabSelected(tab)
            }
        })
        tabLayout?.getTabAt(0)?.select()
    }


    override fun setLocalization() {
        super.setLocalization()
        titleField?.text = RealmHelper.getLocalizedText("preferredclinic.title.text")
        includeNextButton?.nextButtonText?.text =
            RealmHelper.getLocalizedText("preferredclinic.button.next.text")
        scipButtonText?.text = RealmHelper.getLocalizedText("auth.skip")
        backButtonText?.text = SpannableStringBuilder().inSpans(android.text.style.UnderlineSpan()) {
            append(com.example.vinciandroidv2.helper.RealmHelper.getLocalizedText("procedureofinterest.button.back.text"))
        }
    }


    inner class Adapter : RecyclerView.Adapter<Adapter.Holder>() {
        val list = ArrayList<Clinic>()

        @SuppressLint("NotifyDataSetChanged")
        internal fun updateList(list: List<Clinic>) {
            this.list.clear()
            this.list.addAll(list)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(
            LayoutInflater.from(context).inflate(
                R.layout.item_row_drawable_text_view_sample_2, parent, false
            ).apply { startDrawable?.setImageResource(R.drawable.selector_location_brown_grey) }
        )

        override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(list[position])

        override fun getItemCount() = list.size

        inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(clinic: Clinic) {
                itemView.textField?.text = clinic.name ?: ""
                itemView.textField?.text = StringBuilder()
                    .append(clinic.name ?: "")
                    .append(", ")
                    .append(RealmHelper.getCountryById(clinic.countryId ?: -1)?.name ?: "")
                itemView.isSelected = selectedItemId == clinic.id
                itemView.setOnClickListener {
                    selectedItemId = clinic.id
                    notifyDataSetChanged()
                    if (-1 != selectedItemId) {
                        includeNextButton?.isSelected = true
                    }
                    includeNextButton?.setOnClickListener {
                        RealmHelper.getUserData()?.copyFromRealm()?.let {
                            it.clinicId = selectedItemId

                            if (questionnaireFlow == QuestionnaireFlow.AUTH) {
                                it.appState = AppState.WELCOME_STATE.value
                            }

                            userPresenter.editUser(it)
                        }
                    }
                }
            }
        }
    }

    override fun userLoaded() {
//        when (questionnaireFlow) {
//            QuestionnaireFlow.AUTH -> replace(
//                R.id.content,
//                Screens.Auth.getWelcomeFragment()
//            )
//
//            QuestionnaireFlow.PROFILE -> activity?.supportFragmentManager?.popBackStack(
//                GenderAndAgeFragment().TAG, POP_BACK_STACK_INCLUSIVE
//            )
//        }

        if (questionnaireFlow == QuestionnaireFlow.HOME) {
            replace(android.R.id.content, Screens.Auth.getHairPhotosFragment())
        } else {
            replace(android.R.id.content, Screens.Auth.getHairPhotosFragment())
        }
    }
}