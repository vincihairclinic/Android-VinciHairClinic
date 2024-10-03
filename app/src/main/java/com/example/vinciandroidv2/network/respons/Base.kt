package com.example.vinciandroidv2.network.respons

import com.google.gson.annotations.SerializedName

class Base {
    @SerializedName("status_code")
    var status: Int? = 0

    @SerializedName("code")
    var code: Int? = null

    @SerializedName("title")
    var title: String? = null

    @SerializedName("message")
    var message: String? = null
}