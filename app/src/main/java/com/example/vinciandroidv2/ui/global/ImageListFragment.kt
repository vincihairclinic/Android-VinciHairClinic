package com.example.vinciandroidv2.ui.global

import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.vinciandroidv2.R
import kotlinx.android.synthetic.main.fragment_image_list.*
import java.io.File

class ImageListFragment : BaseFragment() {
    var image: String? = null
    var urlImages: ArrayList<String>? = ArrayList()
    override val layoutRes: Int
        get() = R.layout.fragment_image_list
    override val TAG: String
        get() = "ImageListFragment"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (image?.firstOrNull()?.toString() == "h") {
            if (image is String) {
                Glide.with(this)
                    .load(image as String?)
                    .into(imageView)
            }
        } else {
            val file = File(image)
            Glide.with(this).load(File(file.path)).into(imageView)
        }
//        imageView?.setOnClickListener {
//            urlVideos?.forEach {
//                if (it == image) {
//                    startActivity(
//                        Intent(activity, FullScreenVideoActivity::class.java)
//                            .putExtra(
//                                "VIDEO_URL",
//                                image?.toString()
//                            )
//                    )
//                } else {
//                    urlImages?.forEach {
//                        if (it == image) {
//                            this@ImageListFragment.add(
//                                android.R.id.content,
//                                Screens.getFullScreenImageFragment(image as String?)
//                            )
//                        }
//                    }
//
//                }
//            }
//        }

    }

}