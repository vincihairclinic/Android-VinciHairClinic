package com.example.vinciandroidv2.network.respons

import com.example.vinciandroidv2.helper.RealmHelper
import com.google.gson.annotations.SerializedName
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class BaseUser {
    var user: User? = null
}

open class User : RealmObject() {
    @PrimaryKey
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("email")
    var email: String? = null

    @SerializedName("password")
    var password: String? = null


    @SerializedName("is_email_verified")
    var isEmailVerified: Boolean = false


    @SerializedName("onesignal_token")
    var onesignalToken: String? = null

    @SerializedName("version_app")
    var versionApp: String? = null


    @SerializedName("app_state")
    var appState: String? = null

    @SerializedName("gender_id")
    var genderId: Int? = null

    @SerializedName("full_name")
    var fullName: String? = null

    @SerializedName("age")
    var age: Int? = null

    @SerializedName("date_of_birth")
    var dateOfBirth: String? = null

    @SerializedName("phone_number")
    var phoneNumber: String? = null

    @SerializedName("clinic_id")
    var clinicId: Int? = null

    @SerializedName("hair_loss_type_id")
    var hairLossTypeId: Int? = null

    @SerializedName("hair_type_id")
    var hairTypeId: Int? = null

    @SerializedName("country_id")
    var countryId: Int? = null

    @SerializedName("language_key")
    var languageKey: String? = null


    @SerializedName("does_your_family_suffer_from_hereditary_hair_loss")
    var doesYourFamilySufferFromHereditaryHairLoss: Boolean? = null

    @SerializedName("how_long_have_you_experienced_hair_loss_for")
    var howLongHaveYouExperiencedHairLossFor: Int? = null

    @SerializedName("would_you_like_to_get_in_touch_with_you")
    var wouldYouLikeToGetInTouchWithYou: Boolean? = null

    @SerializedName("is_show_news_and_updates_notification")
    var isShowNewsAndUpdatesNotification: Boolean = true

    @SerializedName("is_show_promotions_and_offers_notification")
    var isShowPromotionsAndOffersNotification: Boolean = true

    @SerializedName("is_show_insights_and_tips_notification")
    var isShowInsightsAndTipsNotification: Boolean = true

    @SerializedName("is_show_new_articles_notification")
    var isShowNewArticlesNotification: Boolean = false

    @SerializedName("is_show_requests_and_tickets_notification")
    var isShowRequestsAndTicketsNotification: Boolean = true


    @SerializedName("url_hair_front_image")
    var urlHairFrontImage: String? = null

    @SerializedName("url_hair_side_image")
    var urlHairSideImage: String? = null

    @SerializedName("url_hair_back_image")
    var urlHairBackImage: String? = null

    @SerializedName("url_hair_top_image")
    var urlHairTopImage: String? = null


    @SerializedName("procedures")
    var procedureList: RealmList<Procedure>? = null

    @SerializedName("clinic")
    var clinic: Clinic? = null

    @SerializedName("hair_loss_type")
    var hairLossType: HairLossType? = null

    @SerializedName("hair_type")
    var hairType: HairType? = null

    @SerializedName("country")
    var country: Country? = null

    @SerializedName("language")
    var language: Language? = null
}

class UserProcedureIdList {
    @SerializedName("app_state")
    var appState: String? = null

    @SerializedName("procedure_ids")
    var procedureIdList: ArrayList<Int>? = null
}

open class UserFaceId(
    @PrimaryKey @SerializedName("id") var id: Int = 1,
    @SerializedName("isFaceIdEnabled") var isFaceIdEnabled: Boolean = false
) : RealmObject()