package com.example.vinciandroidv2.ui.global

import android.content.res.Resources
import android.media.MediaPlayer
import com.example.vinciandroidv2.network.respons.PodcastEpisode
import com.example.vinciandroidv2.network.respons.Simulation

const val STATE_SCOPE_NAME = "state_scope_name"

const val API_INTERNET_ERROR = "api_internet_error"
const val API_SERVER_ERROR = "api_server_error"

const val defaultHost = "uk.vinci-hair-clinic-v2.kodetechnologies.com/"

var host: String = defaultHost
val baseUrl: String
    get() = "https://api.${host}"

val privacyPolicyLink: String
    get() = "${baseUrl}/privacy-policy"
val termsOfServiceLink: String
    get() = "${baseUrl}/terms-of-service"

var facebookLink: String? = null
var instagramLink: String? = null
var twitterLink: String? = null
var takeAPhotoVideoLink: String? = null
var mediaPlayer: MediaPlayer? = null
var podcastEpisodeInProgress = PodcastEpisode()
var currentProgress  = 0
var isPlayCheck:  Boolean  = true
var showPlayer:  Boolean  = false
var requestContactFromClinic: String = "https://www.vincihairclinic.com/contact-us/"

var userToken: String? = null

/** uses only when first open app **/
var selectedCountryId: Int = -1

/** uses only when first open app **/
var selectedLanguageKey: String = ""


val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

val Float.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

val Int.sp: Int
    get() = (this / Resources.getSystem().displayMetrics.scaledDensity).toInt()

val Float.sp: Int
    get() = (this / Resources.getSystem().displayMetrics.scaledDensity).toInt()
