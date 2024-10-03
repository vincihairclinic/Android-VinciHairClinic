package com.example.vinciandroidv2.network.respons

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

class BaseLocalization {
    @SerializedName("localizations")
    var localizationList: ArrayList<Localization> = ArrayList()

    @SerializedName("last_timestamp")
    var lastTimestamp: Long? = null
}

open class Localization : RealmObject() {
    @SerializedName("key")
    var key: String? = null

    @SerializedName("value")
    var value: String? = null
}

open class LastTimestamp : RealmObject() {
    @PrimaryKey
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("timestamp")
    var timestamp: Long? = null
}