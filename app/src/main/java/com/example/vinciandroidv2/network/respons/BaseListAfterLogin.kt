package com.example.vinciandroidv2.network.respons

import com.google.gson.annotations.SerializedName
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

class BaseListAfterLogin {
    @SerializedName("Procedure")
    var procedureList: ArrayList<Procedure> = ArrayList()

    @SerializedName("Clinic")
    var clinicList: ArrayList<Clinic> = ArrayList()

    @SerializedName("HairLossType")
    var hairLossTypeList: ArrayList<HairLossType> = ArrayList()

    @SerializedName("HairType")
    var hairTypeList: ArrayList<HairType> = ArrayList()
}

open class Procedure : RealmObject() {
    @PrimaryKey
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("name")
    var name: String? = null

    @SerializedName("url_image")
    var urlImage: String? = null
}

open class HairType : RealmObject() {
    @PrimaryKey
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("name")
    var name: String? = null

    @SerializedName("url_image")
    var urlImage: String? = null
}

open class Clinic : RealmObject() {
    @PrimaryKey
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("territory_id")
    var territoryId: Int? = null

    @SerializedName("country_id")
    var countryId: Int? = null

    @SerializedName("address")
    var address: String? = null

    @SerializedName("postcode")
    var postcode: String? = null

    @SerializedName("lat")
    var lat: Double? = null

    @SerializedName("lng")
    var lng: Double? = null

    @SerializedName("phone_number")
    var phoneNumber: String? = null

    @SerializedName("email")
    var email: String? = null

    @SerializedName("whatsapp")
    var whatsapp: String? = null

    @SerializedName("sort")
    var sort: Long? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("about")
    var about: String? = null

    @SerializedName("benefits")
    var benefitList: RealmList<String>? = null

    @SerializedName("about_clinic_location")
    var aboutClinicLocation: String? = null

    @SerializedName("url_image")
    var urlImage: String? = null

    @SerializedName("url_images")
    var urlImageList: RealmList<String>? = null
}

open class HairLossType : RealmObject() {
    @PrimaryKey
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("name")
    var name: String? = null

    @SerializedName("url_image")
    var urlImage: String? = null

    @SerializedName("gender_id")
    var genderId: Int? = null
}