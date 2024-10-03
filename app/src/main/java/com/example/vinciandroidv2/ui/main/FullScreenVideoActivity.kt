package com.example.vinciandroidv2.ui.main

import android.net.Uri
import android.os.Bundle
import android.view.WindowManager
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.ui.global.BaseActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import kotlinx.android.synthetic.main.activity_full_screen_video_view.*

const val VIDEO_URL_EXTRA = "VIDEO_URL_EXTRA"

class FullScreenVideoActivity : BaseActivity() {
    override val layoutRes = R.layout.activity_full_screen_video_view

    private lateinit var player: ExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        closeButton?.setOnClickListener { finish() }
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        val item = MediaItem.fromUri(Uri.parse(intent?.getStringExtra(VIDEO_URL_EXTRA) ?: ""))

        player = ExoPlayer.Builder(this).build()
        player.trackSelectionParameters =
            player
                .trackSelectionParameters
                .buildUpon()
                .setMaxVideoSize(426, 240)
                .setViewportSize(426, 240, false)
                .build()
        videoView?.player = player

        player.addMediaItem(item)
        player.prepare()
        player.play()
    }

    override fun onPause() {
        super.onPause()
        player.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.stop()
    }
}
