package com.example.vinciandroidv2.ui.main.feed

import android.app.Activity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.text.inSpans
import androidx.core.view.isVisible
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.di.module.ListModule
import com.example.vinciandroidv2.di.module.UserModule
import com.example.vinciandroidv2.helper.ChoosePhoto
import com.example.vinciandroidv2.helper.ImageHelper
import com.example.vinciandroidv2.helper.ImageHelper.setImage
import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.helper.RealmHelper.copyFromRealm
import com.example.vinciandroidv2.network.respons.Simulation
import com.example.vinciandroidv2.ui.Screens
import com.example.vinciandroidv2.ui.auth.questionnaire2.HairPhotoType
import com.example.vinciandroidv2.ui.global.BaseFragment
import com.example.vinciandroidv2.ui.global.VerticalVideoFragment
import com.example.vinciandroidv2.ui.global.mediaPlayer
import com.example.vinciandroidv2.ui.global.mvp.list.ListContract
import com.example.vinciandroidv2.ui.global.mvp.user.UserContract
import com.github.dhaval2404.imagepicker.ImagePicker
import kotlinx.android.synthetic.main.fragment_change_password.*
import kotlinx.android.synthetic.main.fragment_msp_upload_photo.*
import kotlinx.android.synthetic.main.fragment_msp_upload_photo.backButton
import kotlinx.android.synthetic.main.fragment_msp_upload_photo.saveLotti
import kotlinx.android.synthetic.main.view_make_hair_photo_card.view.checkIcon
import kotlinx.android.synthetic.main.view_make_hair_photo_card.view.drawableEnd
import kotlinx.android.synthetic.main.view_make_hair_photo_card.view.drawableStart
import kotlinx.android.synthetic.main.view_make_hair_photo_card.view.textField
import kotlinx.android.synthetic.main.view_next_button.view.nextButtonLottie
import kotlinx.android.synthetic.main.view_next_button.view.nextButtonText
import toothpick.Scope
import toothpick.ktp.delegate.inject
import java.io.File

class MspUploadPhotoFragment(var idSimulatorType: Int = 0) : BaseFragment(), ListContract.View, UserContract.View {
    override val layoutRes: Int
        get() = R.layout.fragment_msp_upload_photo
    override val TAG: String
        get() = "MspUploadPhotoFragment"

    private val listPresenter: ListContract.Presenter by inject()
    private val userPresenter: UserContract.Presenter by inject()
    override fun installModules(scope: Scope) {
        scope.installModules(ListModule(this), UserModule(this))
    }

    private var imageFileList: Array<File?> = Array(4) { null }
    private var choosePhotoFrom = ChoosePhoto.CAMERA
    private var hairPhotoType: HairPhotoType? = null
    var simulation = Simulation()
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bottomBackButton?.setOnClickListener {
            activity?.onBackPressed()
        }
        videoContainer?.setOnClickListener {
            try {
                mediaPlayer?.pause()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            add(android.R.id.content, VerticalVideoFragment())
        }

        backButton?.setOnClickListener { activity?.onBackPressed() }

        includePhotoFrontCard?.apply {
            drawableEnd?.setImageResource(R.drawable.photo_hair_front_light)
            setOnClickListener(HairPhotoType.FRONT)
        }
        includePhotoSideCard?.apply {
            drawableEnd?.setImageResource(R.drawable.photo_hair_side_light)
            setOnClickListener(HairPhotoType.SIDE)
        }
        includePhotoBackCard?.apply {
            drawableEnd?.setImageResource(R.drawable.photo_hair_back_light)
            setOnClickListener(HairPhotoType.BACK)
        }
        includePhotoTopCard?.apply {
            drawableEnd?.setImageResource(R.drawable.photo_hair_top_light)
            setOnClickListener(HairPhotoType.TOP)
        }

    }

    private fun View.setOnClickListener(hairPhotoType: HairPhotoType) {
        setOnClickListener {
            this@MspUploadPhotoFragment.hairPhotoType = hairPhotoType
            if (ImageHelper.permissionListApproved(this@MspUploadPhotoFragment.context)) {
                chooseImageSource()
            } else {
                resultPermissionLauncher.launch(ImageHelper.permissionListToCheck)
            }
        }
    }

    private fun chooseImageSource() {
        pickImage()
    }

    private fun pickImage() {
//        uploadPhotoLottie?.showAnimation(true)
        ImageHelper.imagePickerBuilder(this.requireActivity(), choosePhotoFrom).createIntent {
            imagePickerLauncher.launch(it)
        }
    }

    /** Permission listener **/

    private val resultPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { list ->
        list?.forEach { if (!it.value) return@registerForActivityResult }
        chooseImageSource()
    }

    /** Image listener **/

    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                Activity.RESULT_OK -> result.data?.data?.let { fileUri ->
                    val position = hairPhotoType?.ordinal ?: return@let
                    imageFileList[position] =
                        File(ImageHelper.getRealPathImageFromUri(context, fileUri) ?: "")
                    when (position) {
                        0 -> includePhotoFrontCard
                        1 -> includePhotoSideCard
                        2 -> includePhotoBackCard
                        3 -> includePhotoTopCard
                        else -> return@let
                    }?.drawableStart?.apply {
                        scaleType = ImageView.ScaleType.CENTER_CROP
                        setImage(imageFileList[position]) { isSuccess ->
                        }
                    }


                    when (position) {
                        0 -> includePhotoFrontCard
                        1 -> includePhotoSideCard
                        2 -> includePhotoBackCard
                        3 -> includePhotoTopCard
                        else -> return@let
                    }?.checkIcon?.isVisible = true

                    updateNextButton()
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(context, ImagePicker.getError(result.data), Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
                }
            }
        }

    override fun setLocalization() {
        super.setLocalization()
//        titleField?.text = RealmHelper.getLocalizedText("hairphotos.title.text")
        includePhotoFrontCard?.textField?.text =
            RealmHelper.getLocalizedText("hairphotos.field.front.text")
        includePhotoSideCard?.textField?.text =
            RealmHelper.getLocalizedText("hairphotos.field.side.text")
        includePhotoBackCard?.textField?.text =
            RealmHelper.getLocalizedText("hairphotos.field.back.text")
        includePhotoTopCard?.textField?.text =
            RealmHelper.getLocalizedText("hairphotos.field.top.text")
        nextButtonText?.text =
            RealmHelper.getLocalizedText("hairphotos.button.next.text")
        backButtonText?.text = SpannableStringBuilder().inSpans(UnderlineSpan()) {
            append(RealmHelper.getLocalizedText("procedureofinterest.button.back.text"))
        }
    }

    private fun updateNextButton() {
        if (imageFileList.filterNotNull().size == HairPhotoType.values().size) {
            cardContainer?.isSelected = true
            cardContainer?.setOnClickListener {
                listPresenter.uploadImages(ArrayList<File>().apply { addAll(imageFileList.filterNotNull()) })
            }
        } else {
            cardContainer?.isSelected = false
        }
    }

    override fun imageListUploaded(imageList: ArrayList<String>) {
        simulation.simulationRequestTypeId = idSimulatorType
        simulation.let {
            it.urlHairFrontImage = imageList[HairPhotoType.FRONT.ordinal]
            it.urlHairSideImage = imageList[HairPhotoType.SIDE.ordinal]
            it.urlHairBackImage = imageList[HairPhotoType.BACK.ordinal]
            it.urlHairTopImage = imageList[HairPhotoType.TOP.ordinal]
        }
        replace(android.R.id.content, Screens.Feed.getFillYourDetailsFragment(simulation))

    }

    override fun startLoading() {
        super<BaseFragment>.startLoading()
        cardContainer?.isVisible = false
        saveLotti?.playAnimation()
        saveLotti?.isVisible = true
    }

    override fun stopLoading() {
        cardContainer?.isVisible = true
        super<BaseFragment>.stopLoading()
        saveLotti?.isVisible = false
        saveLotti?.cancelAnimation()
    }

}