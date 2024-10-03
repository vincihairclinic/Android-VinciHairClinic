package com.example.vinciandroidv2.network.respons

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

class BaseList {
    @SerializedName("facebook_url")
    var facebookUrl: String? = null

    @SerializedName("take_a_few_photo_of_your_hair")
    var takePhotoVideoLink: String? = null

    @SerializedName("instagram_url")
    var instagramUrl: String? = null

    @SerializedName("twitter_url")
    var twitterUrl: String? = null

    @SerializedName("Gender")
    var genderList: ArrayList<Gender> = ArrayList()

    @SerializedName("Country")
    var countryList: ArrayList<Country> = ArrayList()

    @SerializedName("Language")
    var languageList: ArrayList<Language> = ArrayList()

    @SerializedName("Territory")
    var territory: ArrayList<Territory> = ArrayList()

    @SerializedName("SimulationRequestType")
    var simulationRequestType: ArrayList<SimulationRequestType> = ArrayList()
}

open class SimulationRequestType : RealmObject() {
    @PrimaryKey
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("name")
    var name: String? = null

    @SerializedName("url_image")
    var urlImage: String? = null
}

open class Gender : RealmObject() {
    @PrimaryKey
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("name")
    var name: String? = null
}

open class Country : RealmObject() {
    @PrimaryKey
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("name")
    var name: String? = null

    @SerializedName("phone_code")
    var phoneCode: String? = null

    @SerializedName("host")
    var host: String? = null

    @SerializedName("shop_url")
    var shopUrl: String? = null

    var clinicNumber: String? = null

    @SerializedName("url_flag_image")
    var urlFlagImage: String? = null

    @SerializedName("url_area_image")
    var urlAreaImage: String? = null
}

open class Language : RealmObject() {
    @PrimaryKey
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("key")
    var key: String? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("url_image")
    var urlImage: String? = null
}









