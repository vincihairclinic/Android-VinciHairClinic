package com.example.vinciandroidv2.ui.auth.questionnaire2

import android.app.Activity
import android.content.Intent
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
import com.example.vinciandroidv2.helper.ImageHelper.imagePickerBuilder
import com.example.vinciandroidv2.helper.ImageHelper.setImage
import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.helper.RealmHelper.copyFromRealm
import com.example.vinciandroidv2.network.respons.AppState
import com.example.vinciandroidv2.ui.Screens
import com.example.vinciandroidv2.ui.global.BaseFragment
import com.example.vinciandroidv2.ui.global.VerticalVideoFragment
import com.example.vinciandroidv2.ui.global.mvp.list.ListContract
import com.example.vinciandroidv2.ui.global.mvp.user.UserContract
import com.example.vinciandroidv2.ui.main.FullScreenVideoActivity
import com.example.vinciandroidv2.ui.main.VIDEO_URL_EXTRA
import com.github.dhaval2404.imagepicker.ImagePicker
import kotlinx.android.synthetic.main.fragment_hair_photos.*
import kotlinx.android.synthetic.main.fragment_hair_photos.backButton
import kotlinx.android.synthetic.main.fragment_hair_photos.includeNextButton
import kotlinx.android.synthetic.main.fragment_hair_photos.scipButton
import kotlinx.android.synthetic.main.fragment_hair_photos.scipButtonText
import kotlinx.android.synthetic.main.fragment_hair_photos.titleField

import kotlinx.android.synthetic.main.view_make_hair_photo_card.view.*
import kotlinx.android.synthetic.main.view_next_button.view.*
import toothpick.Scope
import toothpick.ktp.delegate.inject
import java.io.File

enum class HairPhotoType { FRONT, SIDE, BACK, TOP }

class HairPhotosFragment : BaseFragment(), ListContract.View, UserContract.View {
    override val layoutRes = R.layout.fragment_hair_photos
    override val TAG = "HairPhotosFragment"

    private val listPresenter: ListContract.Presenter by inject()
    private val userPresenter: UserContract.Presenter by inject()
    override fun installModules(scope: Scope) {
        scope.installModules(ListModule(this), UserModule(this))
    }

    private var imageFileList: Array<File?> = Array(4) { null }
    private var choosePhotoFrom = ChoosePhoto.CAMERA
    private var hairPhotoType: HairPhotoType? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        includeNextButton?.setOnClickListener {
            activity?.onBackPressed()
        }
        backButtonText?.setOnClickListener {
            activity?.onBackPressed()
        }
        videoContainer?.setOnClickListener {
            add(android.R.id.content, VerticalVideoFragment())
        }

        scipButton?.setOnClickListener {
            replace(android.R.id.content, Screens.Auth.getGetBackOrCreatePasswordFragment())
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
            this@HairPhotosFragment.hairPhotoType = hairPhotoType
            if (ImageHelper.permissionListApproved(this@HairPhotosFragment.context)) {
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
        imagePickerBuilder(this.requireActivity(), choosePhotoFrom).createIntent {
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
//                            uploadPhotoLottie?.showAnimation(false)
//                            if (!isSuccess) {
//                                Glide.with(this)
//                                    .load(R.drawable.ic_avatar_empty_profile)
//                                    .into(userAvatar)
//                            }
                        }
                    }

//                    when (position) {
//                        0 -> includePhotoFrontCard?.drawableStart?.apply {
//                            scaleType = ImageView.ScaleType.CENTER_CROP
//                            setImageResource(R.drawable.photo_hair_front)
//                        }
//                        1 -> includePhotoSideCard?.drawableStart?.apply {
//                            scaleType = ImageView.ScaleType.CENTER_CROP
//                            setImageResource(R.drawable.photo_hair_side)
//                        }
//                        2 -> includePhotoBackCard?.drawableStart?.apply {
//                            scaleType = ImageView.ScaleType.CENTER_CROP
//                            setImageResource(R.drawable.photo_hair_back)
//                        }
//                        3 -> includePhotoTopCard?.drawableStart?.apply {
//                            scaleType = ImageView.ScaleType.CENTER_CROP
//                            setImageResource(R.drawable.photo_hair_top)
//                        }
//                    }
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
//                    uploadPhotoLottie?.showAnimation(false)
                    Toast.makeText(context, ImagePicker.getError(result.data), Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
//                    uploadPhotoLottie?.showAnimation(false)
//                    Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }

    override fun setLocalization() {
        super.setLocalization()
        titleField?.text = RealmHelper.getLocalizedText("hairphotos.title.text")
        includePhotoFrontCard?.textField?.text =
            RealmHelper.getLocalizedText("hairphotos.field.front.text")
        includePhotoSideCard?.textField?.text =
            RealmHelper.getLocalizedText("hairphotos.field.side.text")
        includePhotoBackCard?.textField?.text =
            RealmHelper.getLocalizedText("hairphotos.field.back.text")
        includePhotoTopCard?.textField?.text =
            RealmHelper.getLocalizedText("hairphotos.field.top.text")
        includeNextButton?.nextButtonText?.text =
            RealmHelper.getLocalizedText("hairphotos.button.next.text")
        scipButtonText?.text = RealmHelper.getLocalizedText("auth.skip")
        backButtonText?.text = SpannableStringBuilder().inSpans(UnderlineSpan()) {
            append(RealmHelper.getLocalizedText("procedureofinterest.button.back.text"))
        }
    }

    private fun updateNextButton() {
        if (imageFileList.filterNotNull().size == HairPhotoType.values().size) {
            includeNextButton?.isSelected = true
            includeNextButton?.setOnClickListener {
                listPresenter.uploadImages(ArrayList<File>().apply { addAll(imageFileList.filterNotNull()) })
            }
        } else {
            includeNextButton?.isSelected = false
            includeNextButton?.setOnClickListener(null)
        }
    }

    override fun imageListUploaded(imageList: ArrayList<String>) {
        RealmHelper.getUserData()?.copyFromRealm()?.let {
            it.urlHairFrontImage = imageList[HairPhotoType.FRONT.ordinal]
            it.urlHairSideImage = imageList[HairPhotoType.SIDE.ordinal]
            it.urlHairBackImage = imageList[HairPhotoType.BACK.ordinal]
            it.urlHairTopImage = imageList[HairPhotoType.TOP.ordinal]
//            it.appState = AppState.COMPLETED_STATE.value
            userPresenter.editUser(it)
        }
    }

    override fun userLoaded() {
        replace(android.R.id.content, Screens.Auth.getGetBackOrCreatePasswordFragment())
    }

    override fun startLoading() {
        super<BaseFragment>.startLoading()
        includeNextButton?.isEnabled = false
        includeNextButton?.nextButtonText?.isVisible = false
        includeNextButton?.nextButtonLottie?.playAnimation()
        includeNextButton?.nextButtonLottie?.isVisible = true
    }

    override fun stopLoading() {
        super<BaseFragment>.stopLoading()
        includeNextButton?.isEnabled = true
        includeNextButton?.nextButtonText?.isVisible = true
        includeNextButton?.nextButtonLottie?.cancelAnimation()
        includeNextButton?.nextButtonLottie?.isVisible = false
    }
}