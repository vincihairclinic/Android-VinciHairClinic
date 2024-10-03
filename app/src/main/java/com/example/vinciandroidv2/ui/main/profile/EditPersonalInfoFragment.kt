package com.example.vinciandroidv2.ui.main.profile

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.UnderlineSpan
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.core.text.inSpans
import androidx.core.view.isVisible
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.di.module.UserModule
import com.example.vinciandroidv2.helper.*
import com.example.vinciandroidv2.helper.RealmHelper.copyFromRealm
import com.example.vinciandroidv2.helper.RealmHelper.realm
import com.example.vinciandroidv2.network.respons.Gender
import com.example.vinciandroidv2.network.respons.User
import com.example.vinciandroidv2.ui.Screens
import com.example.vinciandroidv2.ui.global.BaseFragment
import com.example.vinciandroidv2.ui.global.getAge
import com.example.vinciandroidv2.ui.global.mvp.user.UserContract
import com.example.vinciandroidv2.ui.global.setToStringFormat
import com.example.vinciandroidv2.ui.main.MainActivity
import com.google.android.material.card.MaterialCardView
import io.realm.RealmList
import kotlinx.android.synthetic.main.fragment_edit_personal_info.*
import kotlinx.android.synthetic.main.item_row_edit_text_sample_1.view.*
import kotlinx.android.synthetic.main.item_row_text_view_sample_1.view.*
import kotlinx.android.synthetic.main.view_next_button.view.*
import toothpick.Scope
import toothpick.ktp.delegate.inject
import java.util.*

class EditPersonalInfoFragment : BaseFragment(), UserContract.View {
    override val layoutRes = R.layout.fragment_edit_personal_info
    override val TAG = "EditPersonalInfoFragment"

    private val userPresenter: UserContract.Presenter by inject()
    override fun installModules(scope: Scope) {
        scope.installModules(UserModule(this))
    }

    private val userListener = RealmHelper.realm?.where(User::class.java)?.findAll()
    private val genderList = RealmHelper.realm?.where(Gender::class.java)?.findAll() ?: RealmList()
    private var selectedGender: Gender? = null
        set(value) {
            field = value
            includeGenderCard?.textField?.text = selectedGender?.name ?: ""
        }
    private var isChange: Boolean = false
    private val ageList = ArrayList<String>().apply { for (i in 1..150) add("$i") }.toTypedArray()
    private var selectedAge: String? = null


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        this@EditPersonalInfoFragment.includeEmailCard?.editTextField?.let { textListener(it) }
//        this@EditPersonalInfoFragment.includeFullNameCard?.editTextField?.let { textListener(it) }
        includeNextButton?.setOnClickListener {
            if (isValidForm()) {
                RealmHelper.getUserData()?.copyFromRealm()?.let {
                    it.fullName =
                        this@EditPersonalInfoFragment.includeFullNameCard?.editTextField?.text?.toString()
                    it.genderId = selectedGender?.id
                    it.dateOfBirth = selectedAge
                    it.email = this@EditPersonalInfoFragment.includeEmailCard?.editTextField?.text?.toString()
                    userPresenter.editUser(it)
                }
            }
        }
        progressBar?.progress = 50
        RealmHelper.getUserData()?.copyFromRealm().let {
            if (it?.fullName.isNullOrEmpty() && it?.email.isNullOrEmpty()) {
                checkWhotShow(true)
                saveButton?.isVisible = false
                includeEmailCard?.editTextField?.hint =
                    RealmHelper.getLocalizedText("profile.personalinfo.email.field.hint")
                includeFullNameCard?.editTextField?.hint =
                    RealmHelper.getLocalizedText("profile.personalinfo.name.field.hint")
                activity?.getDrawable(R.drawable.ic_cross)?.let { img ->
                    imageView2?.setImageDrawable(img)
                }
                isChange = true
            } else {
                activity?.getDrawable(R.drawable.ic_arrow_left_brown_16)?.let { img ->
                    imageView2?.setImageDrawable(img)
                }
                isChange = false
                saveButton?.isVisible = true
                checkWhotShow(false)
            }
        }
        backButton?.setOnClickListener { activity?.onBackPressed() }
        saveButton?.apply {
            isSelected = true
            setOnClickListener {
                hideKeyboard()
                if (isValidForm()) {
                    RealmHelper.getUserData()?.copyFromRealm()?.let {
                        it.fullName =
                            this@EditPersonalInfoFragment.includeFullNameCard?.editTextField?.text?.toString()
                        it.genderId = selectedGender?.id
                        it.dateOfBirth = selectedAge
                        userPresenter.editUser(it)
                    }
                }
            }
        }
        includeFullNameCard?.editTextField?.apply {
            setOnFocusChangeListener { v, hasFocus ->
                (v?.parent as? MaterialCardView)?.isSelected = hasFocus
            }
        }
        includeGenderCard?.textField?.apply {
            setEndDrawable(R.drawable.ic_arrow_down_brown_24)
            setOnClickListener {
                showSimpleListDialog(
                    this@EditPersonalInfoFragment.context,
                    genderList.map { it.name ?: "" }.toTypedArray()
                ) { _, newItem -> selectedGender = genderList[newItem] }
                isValidFormNextBtn()

            }
        }
//        includeAgeCard?.textField?.apply {
//
//            setOnClickListener {
//                addDateBirth()
//            }
//        }


        includeEmailCard?.editTextField?.apply {
            RealmHelper.getUserData().let {
                isEnabled = it?.email.isNullOrEmpty()
            }
            inputType = EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            setOnFocusChangeListener { v, hasFocus ->
                (v?.parent as? MaterialCardView)?.isSelected = hasFocus
            }
        }
        includeChangePasswordCard?.apply {
            textField?.setStartDrawable(R.drawable.ic_lock_round_brown_24)
            setOnClickListener {
                add(R.id.content, Screens.Profile.getChangePasswordFragment())
            }
        }
        includeDeleteAccountCard?.apply {
            textField?.setStartDrawable(R.drawable.ic_cross_round_brown_24)
            setOnClickListener {
                userPresenter.deleteAccount()
            }
        }

        userListener?.addChangeListener { _ -> updateViews() }
        updateViews()
        addDateBirth()
    }

    override fun userLoaded() {
        super.userLoaded()
        if (isChange) {
            replace(R.id.content, Screens.Auth.getCreatePasswordFragment())
        }
    }

    fun checkWhotShow(isEmptyDate: Boolean = false) {
        if (isEmptyDate) {
            mainContainer?.isVisible = true
            includeNextButton?.isVisible = true
            shadowBottomBar?.isVisible = true
            includeDeleteAccountCard?.isVisible = false
            includeChangePasswordCard?.isVisible = false
            divider3?.isVisible = false
        } else {
            includeDeleteAccountCard?.isVisible = true
            includeChangePasswordCard?.isVisible = true
            divider3?.isVisible = true
            mainContainer?.isVisible = false
            includeNextButton?.isVisible = false
            shadowBottomBar?.isVisible = false
        }

    }

    fun addDateBirth() {
        includeAgeCard?.textField?.apply {
            text = RealmHelper.getUserData()?.age?.toString() ?: ""
            setEndDrawable(R.drawable.ic_arrow_down_brown_24)
            setOnClickListener {
                val dialog = datePickerDialog()
                dialog.datePicker.maxDate = Date().time
                dialog.setTitle("Birthday")
                dialog.show()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun datePickerDialog(): DatePickerDialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), R.style.ThemeMyTheme, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            val calendar = Calendar.getInstance()?.apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, monthOfYear)
                set(Calendar.DAY_OF_MONTH, dayOfMonth)
            }
//            realm?.executeTransaction { nRealm ->
//                nRealm.where(User::class.java)?.findFirst()?.let {
//                    it.dateOfBirth = calendar?.time?.setToStringFormat("yyyy-MM-dd")
//                }
//            }
            selectedAge = calendar?.time?.setToStringFormat("yyyy-MM-dd").toString()
            includeAgeCard?.textField?.text = calendar?.time?.setToStringFormat("dd MMMM yyyy")?.let { getAge(it) }.toString()

            isValidFormNextBtn()
        }, year, month, day)
        return datePickerDialog
    }

    private fun updateViews() {
        RealmHelper.getUserData()?.copyFromRealm()?.let { user ->
            this@EditPersonalInfoFragment.includeEmailCard?.editTextField?.setText(user.email)
            this@EditPersonalInfoFragment.includeFullNameCard?.editTextField?.setText(user.fullName)
            selectedGender = genderList.firstOrNull { it.id == user.genderId }
            selectedAge = user.dateOfBirth.toString() ?: ""
            this@EditPersonalInfoFragment.includeAgeCard?.textField?.text = if (user.age.toString() != null) {
                user.age.toString() ?: ""
            } else {
                ""
            }

        }
    }

    override fun accountDeleted() {
//        RealmHelper.removeSensitiveDataFromBase()
//        (activity?.application as App).updateAppScope()

//        startActivity(Intent(context, SplashActivity::class.java))
//        activity?.finish()

        (activity as? MainActivity)?.logOutUser()
    }

    override fun setLocalization() {
        super.setLocalization()
        saveButton?.text = SpannableStringBuilder().inSpans(UnderlineSpan()) {
            append(RealmHelper.getLocalizedText("profile.personalinfo.button.save.text"))
        }
        RealmHelper.getUserData()?.copyFromRealm().let {
            if (it?.fullName.isNullOrEmpty() && it?.email.isNullOrEmpty()) {
                titleField?.text = RealmHelper.getLocalizedText("profile.setup.title.text")
            } else {
                titleField?.text = RealmHelper.getLocalizedText("profile.personalinfo.title.text")
            }
        }
//        titleField?.text = RealmHelper.getLocalizedText("profile.personalinfo.title.text")
        fullNameLabel?.text = RealmHelper.getLocalizedText("profile.personalinfo.name.label.text")
        genderLabel?.text = RealmHelper.getLocalizedText("profile.personalinfo.gender.label.text")
        ageLabel?.text = RealmHelper.getLocalizedText("profile.personalinfo.age.label.text")
        emailLabel?.text = RealmHelper.getLocalizedText("profile.personalinfo.email.label.text")
//        includeFullNameCard?.editTextField?.hint =
//            RealmHelper.getLocalizedText("profile.personalinfo.name.field.hint")
//        includeEmailCard?.editTextField?.hint =
//            RealmHelper.getLocalizedText("profile.personalinfo.email.field.hint")
        includeChangePasswordCard?.textField?.text =
            RealmHelper.getLocalizedText("profile.personalinfo.button.password.text")
        includeDeleteAccountCard?.textField?.text =
            RealmHelper.getLocalizedText("profile.personalinfo.button.delete.text")
        includeNextButton?.nextButtonText?.text =
            RealmHelper.getLocalizedText("gender.button.next.text")
    }

    private fun isValidForm() = when {
        (includeFullNameCard?.editTextField?.text?.length ?: 0) < 2 -> {
            showBaseAlert(
                context,
                title = String.format("Full Name must be more then 2 symbols"),
                positiveText = getString(R.string.ok_label)
            )
            false
        }
//        includeEmailCard?.editTextField?.text?.length ?: 0 < 2 -> {
//            showBaseAlert(
//                context,
//                title = String.format("Email must be more then 2 symbols"),
//                positiveText = getString(R.string.ok_label)
//            )
//            false
//        }
        else -> true
    }

    private fun isValidFormNextBtn() {
        includeNextButton?.isSelected = (!this@EditPersonalInfoFragment.includeAgeCard?.textField?.text.isNullOrEmpty()
                && !this@EditPersonalInfoFragment.includeGenderCard?.textField?.text.isNullOrEmpty()
                && !this@EditPersonalInfoFragment.includeFullNameCard?.editTextField?.text.isNullOrEmpty()
                && !this@EditPersonalInfoFragment.includeEmailCard?.editTextField?.text.isNullOrEmpty())
        includeNextButton?.isEnabled = includeNextButton?.isSelected ?: false
    }

    fun textListener(editText: EditText) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                isValidFormNextBtn()
            }
        })
    }

    override fun startLoading() {
        super<BaseFragment>.startLoading()
        if (isChange) {

        } else {
            saveButton?.isVisible = false
            saveLotti?.playAnimation()
            saveLotti?.isVisible = true
        }

    }

    override fun stopLoading() {
        super<BaseFragment>.stopLoading()
        if (isChange) {

        } else {
            saveButton?.isVisible = true
            saveLotti?.isVisible = false
            saveLotti?.cancelAnimation()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        userListener?.removeAllChangeListeners()
    }
}