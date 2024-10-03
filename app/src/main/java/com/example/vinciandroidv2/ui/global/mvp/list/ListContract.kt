package com.example.vinciandroidv2.ui.global.mvp.list

import com.example.vinciandroidv2.ui.global.mvp.BaseView
import java.io.File

interface ListContract {
    interface View : BaseView {
        fun listsLoaded() {}
        fun listsAfterLoginLoaded() {}
        fun imageListUploaded(imageList: ArrayList<String>) {}
        fun videoUploaded() {}
        fun geocodeListLoaded() {}
        fun addressLoaded() {}

        fun localizationsLoaded() {}
    }

    interface Presenter {
        fun getLists()
        fun getListsAfterLogin()
        fun uploadImages(fileList: ArrayList<File>)
        fun getGeocodeByAutoComplete(input: String)
        fun getAddressByLatLng(lat: Double, lng: Double)

        fun getLocalization(languageKey: String = "en")
    }
}