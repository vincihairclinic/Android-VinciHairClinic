package com.example.vinciandroidv2.helper

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.view.View
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.FragmentActivity
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.github.dhaval2404.imagepicker.ImagePicker
import com.makeramen.roundedimageview.RoundedImageView
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

enum class ChoosePhoto { CAMERA, GALLERY }

object ImageHelper {
    fun RoundedImageView.setImage(
        file: File?,
        onLoadSuccess: ((isSuccess: Boolean) -> Unit)? = null
    ) {
        Glide.with(this)
            .load(file)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    onLoadSuccess?.let { it(false) }
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    onLoadSuccess?.let { it(true) }
                    return false
                }
            })
            .into(this)
    }

//    fun ImageView.setImage(
//        file: File?,
//        onLoadSuccess: ((isSuccess: Boolean) -> Unit)? = null
//    ) {
//        Glide.with(this)
//            .load(file)
//            .listener(object : RequestListener<Drawable> {
//                override fun onLoadFailed(
//                    e: GlideException?,
//                    model: Any?,
//                    target: Target<Drawable>?,
//                    isFirstResource: Boolean
//                ): Boolean {
//                    onLoadSuccess?.let { it(false) }
//                    return false
//                }
//
//                override fun onResourceReady(
//                    resource: Drawable?,
//                    model: Any?,
//                    target: Target<Drawable>?,
//                    dataSource: DataSource?,
//                    isFirstResource: Boolean
//                ): Boolean {
//                    onLoadSuccess?.let { it(true) }
//                    return false
//                }
//            })
//            .into(this)
//    }

    fun imagePickerBuilder(context: FragmentActivity, choosePhotoFrom: ChoosePhoto) =
        ImagePicker.with(context)
            .apply {
                when (choosePhotoFrom) {
                    ChoosePhoto.CAMERA -> {
                        cameraOnly()
                    }
                    ChoosePhoto.GALLERY -> {
                        galleryOnly()
                        galleryMimeTypes(  //Exclude gif images
                            mimeTypes = arrayOf(
                                "image/png",
                                "image/jpg",
                                "image/jpeg"
                            )
                        )
                    }
                }
            }
//            .compress(1024)         //Final image size will be less than 1 MB(Optional)
//            .maxResultSize(
//                1080,
//                1080
//            )  //Final image resolution will be less than 1080 x 1080(Optional)
            .saveDir(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!)

    fun Activity.ImagePickerBuilder(choosePhoto: ChoosePhoto) =
        ImagePicker.with(this)
            .apply {
                when (choosePhoto) {
                    ChoosePhoto.CAMERA -> cameraOnly()
                    ChoosePhoto.GALLERY -> {
                        galleryOnly()
                        galleryMimeTypes(  //Exclude gif images
                            mimeTypes = arrayOf(
                                "image/png",
                                "image/jpg",
                                "image/jpeg"
                            )
                        )
                    }
                }
            }
            .compress(1024)         //Final image size will be less than 1 MB(Optional)
            .maxResultSize(
                1080,
                1080
            )  //Final image resolution will be less than 1080 x 1080(Optional)
            .saveDir(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!)


    /** Permissions **/

    fun permissionListApproved(context: Context?): Boolean {
        permissionListToCheck.forEach { permission ->
            if (PackageManager.PERMISSION_GRANTED !=
                context?.let { checkSelfPermission(it, permission) }
            ) return false
        }
        return true
    }

    val permissionListToCheck = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )


    /** Path to photo **/

    fun getRealPathImageFromUri(context: Context?, uri: Uri?): String? {
        if (null == context) return null

        val file = File(
            context.applicationInfo.dataDir +
                    File.separator +
                    System.currentTimeMillis()
        )

//    textFieldTwo?.text = context.applicationInfo.dataDir
//    textFieldThree?.text = file.absolutePath
//    textFieldFour?.text = file.canonicalPath
//    textFieldFive?.text = file.path
        try {
            val inputStream = context.contentResolver.openInputStream(uri!!) ?: return null
            val outputStream: OutputStream = FileOutputStream(file)

            val buf = ByteArray(1024)
            var len: Int

            while (inputStream.read(buf).also { len = it } > 0)
                outputStream.write(buf, 0, len)

            outputStream.close()
            inputStream.close()
        } catch (ignore: IOException) {
            return null
        }
        return file.absolutePath
    }


    /** Show lotti **/

    fun LottieAnimationView?.showAnimation(isShow: Boolean) {
        if (isShow) {
            this?.playAnimation()
            this?.visibility = View.VISIBLE
//            imageLotti?.addValueCallback(
//                KeyPath("**"), LottieProperty.COLOR_FILTER
//            ) {
//                PorterDuffColorFilter(
//
//                    imageContainer.background,
//                    PorterDuff.Mode.SRC_ATOP
//                )
//            }
        } else {
            this?.cancelAnimation()
            this?.visibility = View.GONE
        }
    }
}