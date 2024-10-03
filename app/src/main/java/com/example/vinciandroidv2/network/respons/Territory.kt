package com.example.vinciandroidv2.network.respons

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Territory : RealmObject() {
    @PrimaryKey
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("name")
    var name: String? = null

    @SerializedName("url_area_image")
    var urlAreaImage: String? = null
}