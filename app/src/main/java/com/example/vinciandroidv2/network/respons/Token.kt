package com.example.vinciandroidv2.network.respons

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Token : RealmObject() {
    @PrimaryKey
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("flow")
    var googleFlow: String? = null

    @SerializedName("token_type")
    var tokenType: String? = null

    @SerializedName("token")
    var accessToken: String? = null

    @SerializedName("user")
    var user: User? = null
}