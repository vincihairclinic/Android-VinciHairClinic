package com.example.vinciandroidv2.network.respons

import com.google.gson.annotations.SerializedName

open class Simulation {
    @SerializedName("full_name")
    var fullName: String? = null

    @SerializedName("email")
    var email: String? = null

    @SerializedName("country_id")
    var countryId: Int? = null

    @SerializedName("simulation_request_type_id")
    var simulationRequestTypeId: Int? = null

    @SerializedName("phone_number")
    var phoneNumber: String? = null

    @SerializedName("url_image")
    var urlImage: String? = null

    @SerializedName("url_hair_front_image")
    var urlHairFrontImage: String? = null

    @SerializedName("url_hair_side_image")
    var urlHairSideImage: String? = null

    @SerializedName("url_hair_back_image")
    var urlHairBackImage: String? = null

    @SerializedName("url_hair_top_image")
    var urlHairTopImage: String? = null
}