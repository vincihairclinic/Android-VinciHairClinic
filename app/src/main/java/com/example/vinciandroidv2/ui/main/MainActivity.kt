package com.example.vinciandroidv2.ui.main

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Metadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.session.SessionToken
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.helper.ImageHelper.ImagePickerBuilder
import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.helper.createBookList
import com.example.vinciandroidv2.network.respons.Podcast
import com.example.vinciandroidv2.network.respons.PodcastEpisode
import com.example.vinciandroidv2.ui.Screens
import com.example.vinciandroidv2.ui.global.*
import com.example.vinciandroidv2.ui.global.mvp.list.ListContract
import com.example.vinciandroidv2.ui.global.mvp.user.UserContract
import com.example.vinciandroidv2.ui.main.clinics.ClinicsFragment
import com.example.vinciandroidv2.ui.main.feed.FeedFragment
import com.example.vinciandroidv2.ui.main.home.HomeFragment
import com.example.vinciandroidv2.ui.main.store.StoreFragment
import com.google.android.material.navigation.NavigationBarView
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URI

enum class NavTabAction { FIND_CLINIC_NEARLY }

class MainActivity : BaseActivity(), UserContract.View, ListContract.View {
    override val layoutRes = R.layout.activity_main
    private lateinit var controllerFuture: ListenableFuture<MediaController>
    private lateinit var controller: MediaController

    private var pageAdapter = ViewPagerAdapter(supportFragmentManager).apply {
        addFragment(Screens.Hosts.getHomeHost(), "")
        addFragment(Screens.Hosts.getClinicsHost(), "")
        addFragment(Screens.Hosts.getStoreHost(), "")
        addFragment(Screens.Hosts.getFeedHost(), "")
    }


    private val onPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {
            when (position) {
                0 -> (findFragment(HomeFragment().TAG) as? HomeFragment)?.updateHomeData()
//                1 -> (findFragment(ClinicsFragment().TAG) as? ClinicsFragment)
                2 -> (findFragment(StoreFragment().TAG) as? StoreFragment)?.updateStoreData()
                3 -> (findFragment(FeedFragment().TAG) as? FeedFragment)?.updateFeedData()
            }
        }

        override fun onPageScrollStateChanged(state: Int) {
        }
    }

    private val onNavigationItemListener = NavigationBarView.OnItemSelectedListener { item ->
        when (item.itemId) {
            R.id.homeNavItem -> viewPager?.currentItem = 0
            R.id.clinicsNavItem -> viewPager?.currentItem = 1
            R.id.storeNavItem -> viewPager?.currentItem = 2
            R.id.feedNavItem -> viewPager?.currentItem = 3
        }
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewPager?.apply {
            adapter = pageAdapter
            offscreenPageLimit = pageAdapter.count
            addOnPageChangeListener(onPageChangeListener)
        }
        bottomNavigationView?.setOnItemSelectedListener(onNavigationItemListener)

        createBookList(this)
    }


    override fun setLocalization() {
        super.setLocalization()
        bottomNavigationView?.apply {
            menu.getItem(0)?.title = RealmHelper.getLocalizedText("main.navigation.item.text.one")
            menu.getItem(1)?.title = RealmHelper.getLocalizedText("main.navigation.item.text.two")
            menu.getItem(2)?.title = RealmHelper.getLocalizedText("main.navigation.item.text.three")
            menu.getItem(3)?.title = RealmHelper.getLocalizedText("main.navigation.item.text.four")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewPager?.removeOnPageChangeListener(onPageChangeListener)
    }

    fun setNavViewAction(navTabAction: NavTabAction) {
        when (navTabAction) {
            NavTabAction.FIND_CLINIC_NEARLY -> {
                bottomNavigationView?.selectedItemId = R.id.clinicsNavItem
                (findFragment(ClinicsFragment().TAG) as? ClinicsFragment)
                    ?.view
                    ?.findViewById<View>(R.id.includeNearMeButton)
                    ?.performClick()
            }
        }
    }

    internal fun hidePlayer() {
        player?.isVisible = false
    }
    internal fun showPlayer() {
        player?.isVisible = true
    }
    override fun onStart() {
        super.onStart()
        val sessionToken = SessionToken(this, ComponentName(this, PlaybackService::class.java))
        controllerFuture = MediaController.Builder(this, sessionToken).buildAsync()
        controllerFuture.addListener(
            {
                controller = controllerFuture.get()
                initController()
            },
            MoreExecutors.directExecutor()
        )
    }

    private fun initController() {
        //controller.playWhenReady = true
        controller.addListener(object : Player.Listener {

            override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                super.onMediaMetadataChanged(mediaMetadata)
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
            }

            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)
            }

            override fun onPlayerErrorChanged(error: PlaybackException?) {
                super.onPlayerErrorChanged(error)
            }
        })
    }

    private fun play(url: String) {
        val uri = Uri.parse(podcastEpisodeInProgress.urlImage)
        val metadata = MediaMetadata.Builder()
            .setArtist(podcastEpisodeInProgress.name)
            .setArtworkUri(uri)
            .build()
        val media = MediaItem.Builder().setMediaId(url).setMediaMetadata(metadata).build()
        controller.setMediaItem(media)
        controller.prepare()
        controller.play()

        update(controller, progressBar, this)

        val myUri = Uri.parse(url)
        try {
            mediaPlayer?.setDataSource(this, myUri)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        mediaPlayer?.setOnPreparedListener { mp ->
            progressBar.max = mp.duration
        }
    }

    fun startStop(isPlay: Boolean = true) {
        if (isPlay) {
            controller.play()
            playAudio?.setImageResource(R.drawable.ic_pausa_small)
        } else {
            controller.pause()
            playAudio?.setImageResource(R.drawable.ic_play_smal)
        }
    }

    fun stopPlayer() {
        controller.stop()
        mediaPlayer?.stop()
    }

    override fun onStop() {
        MediaController.releaseFuture(controllerFuture)
        super.onStop()
    }

    internal fun showAndPlayAudio(podcast: Podcast, podcastEpisode: PodcastEpisode, isPodscat: Boolean = false, isDetailesNot: Boolean = true) {
        player?.isVisible = isDetailesNot
        var isPlay: Boolean? = true
        isPlayCheck = false

        if (true == isPlay) {
            playAudio?.setImageResource(R.drawable.ic_pausa_small)
        } else {
            playAudio?.setImageResource(R.drawable.ic_play_smal)
        }
        if (isPodscat) {
            Glide.with(this).load(podcast.urlImage).into(imagePodcastMain)
            namePodcastMain?.text = podcast.name
        } else {
            Glide.with(this).load(podcastEpisode.urlImage)
                .into(imagePodcastMain)
            namePodcastMain?.text = podcastEpisode.name
        }
        if (controller.isPlaying) {
            controller.stop()
            mediaPlayer?.stop()
        }
        val fileSet = podcastEpisodeInProgress.urlFile
        fileSet?.let {
            play(it)
        }
        playAudio?.setOnClickListener {
            if (true == isPlay) {
                isPlay = false
                startStop(true)
                isPlayCheck = true
            } else {
                isPlay = true
                isPlayCheck = false
                startStop(false)
            }
        }
        close?.setOnClickListener {
            player?.isVisible = false
            controller.stop()
        }
    }

    private fun update(
        mediaPlayer: MediaController,
        progressBar: ProgressBar,
        context: Context
    ) {
        (context as Activity).runOnUiThread {
            val progres = mediaPlayer.currentPosition / 1000
            progressBar.progress = progres.toInt()
            currentProgress = progres.toInt()
            val handler = Handler()
            try {
                val runnable = Runnable {
                    try {
                        if (mediaPlayer.currentPosition > -1) {
                            try {
                                update(mediaPlayer, progressBar, context)
                            } catch (e: java.lang.Exception) {
                                e.printStackTrace()
                            }
                        }
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }
                handler.postDelayed(runnable, 500)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onBackPressed() {
        val sfm = supportFragmentManager

        if (sfm.backStackEntryCount > pageAdapter.count) {
            val first = sfm.fragments.lastOrNull()
            when ((first?.view?.parent as? ViewGroup)?.id) {
                R.id.homeNavItem -> {
                    if (viewPager?.currentItem == 0) {
                        sfm.popBackStack()
                    }
                    viewPager?.currentItem = 0
                }
                R.id.clinicsNavItem -> {
                    if (viewPager?.currentItem == 1) {
                        sfm.popBackStack()
                    }
                    viewPager?.currentItem = 1
                }
                R.id.storeNavItem -> {
                    if (viewPager?.currentItem == 2) {
                        sfm.popBackStack()
                    }
                    viewPager?.currentItem = 2
                }
                R.id.feedNavItem -> {
                    if (viewPager?.currentItem == 3) {
                        sfm.popBackStack()
                    }
                    viewPager?.currentItem = 3
                }

                else -> first?.tag.let {
                    when (sfm.getBackStackEntryAt(sfm.backStackEntryCount - 1).name) {
//                            "WorkoutVerticalVideoFragment" -> {
//                                val oft =
//                                    (sfm.findFragmentByTag("WorkoutVerticalVideoFragment") as WorkoutVerticalVideoFragment)
//                                        .openFirstTime
//                                if (oft) sfm.popBackStack()
//                            }
//                            "MultiplayerVerticalVideoFragment" -> {
//                                val oft =
//                                    (sfm.findFragmentByTag("MultiplayerVerticalVideoFragment") as MultiplayerVerticalVideoFragment)
//                                        .openFirstTime
//                                if (oft) sfm.popBackStack()
//                            }
//                            "FreestyleInProgressFragment" -> {
//                            }
//                            "InviteFragment" -> {
//                                (sfm.findFragmentByTag("OnDemandFragment") as OnDemandFragment).checkInfoOnDemand()
//                                sfm.popBackStack()
//                            }
//                            "WorkoutStartFragment" -> {
//                                (sfm.findFragmentByTag("OnDemandFragment") as OnDemandFragment).checkInfoOnDemand()
//                                sfm.popBackStack()
//                            }
//                            "WorkoutStartFragment" -> {
//                                (sfm.findFragmentByTag("WorkoutPackInfoFragment") as WorkoutPackInfoFragment).getCategory()
//                                sfm.popBackStack()
//                            }
//                            "WorkoutStartFragment" -> sfm.popBackStack()
                        else -> sfm.popBackStack()
                    }
                }
            }
        } else finish()
    }
}

class PlaybackService : MediaSessionService(), MediaSession.Callback {

    private var mediaSession: MediaSession? = null

    override fun onCreate() {
        super.onCreate()
        val player = ExoPlayer.Builder(this).build()
        mediaSession = MediaSession.Builder(this, player).setCallback(this).build()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? =
        mediaSession

    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }

    override fun onAddMediaItems(
        mediaSession: MediaSession,
        controller: MediaSession.ControllerInfo,
        mediaItems: MutableList<MediaItem>
    ): ListenableFuture<MutableList<MediaItem>> {
        val updatedMediaItems = mediaItems.map { it.buildUpon().setUri(it.mediaId).build() }.toMutableList()
        return Futures.immediateFuture(updatedMediaItems)
    }
}