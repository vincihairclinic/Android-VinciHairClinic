package com.example.vinciandroidv2.ui.global

import android.net.Uri
import android.os.Bundle
import android.widget.MediaController
import com.example.vinciandroidv2.R
import kotlinx.android.synthetic.main.fragment_vertical_video.videoView

class VerticalVideoFragment: BaseFragment() {
    override val layoutRes: Int
        get() = R.layout.fragment_vertical_video
    override val TAG: String
        get() = "VerticalVideoFragment"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val uri = Uri.parse(takeAPhotoVideoLink)
        videoView?.setVideoURI(uri)
        val mediaController = MediaController(requireContext())
        mediaController.setAnchorView(videoView)
        mediaController.setMediaPlayer(videoView)
        videoView.setMediaController(mediaController)
        videoView.start()
    }
}