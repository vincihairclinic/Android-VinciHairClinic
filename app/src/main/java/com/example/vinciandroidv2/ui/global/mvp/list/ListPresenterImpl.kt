package com.example.vinciandroidv2.ui.global.mvp.list

import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.network.ApiService
import com.example.vinciandroidv2.network.ResponseHandler
import com.example.vinciandroidv2.network.respons.*
import com.example.vinciandroidv2.ui.global.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.RealmObject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import toothpick.InjectConstructor
import java.io.File

@InjectConstructor
class ListPresenterImpl(
    private val view: ListContract.View,
    private val apiService: ApiService
) : ListContract.Presenter {
    override fun getLists() {
        apiService.getLists()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .doOnSubscribe { view.startLoading() }
            .doOnSuccess { view.stopLoading() }
            .doOnError { view.stopLoading() }
            .subscribe(object : ResponseHandler<BaseList>() {
                override fun onSuccess(response: BaseList) {
                    RealmHelper.realm?.where(Gender::class.java)?.findAll()?.let {
                        val temp = ArrayList<RealmObject>()
                        temp.addAll(it)
                        RealmHelper.remove(temp)
                    }
                    RealmHelper.saveObjects(response.genderList)
                    RealmHelper.realm?.where(SimulationRequestType::class.java)?.findAll()?.let {
                        val temp = ArrayList<RealmObject>()
                        temp.addAll(it)
                        RealmHelper.remove(temp)
                    }
                    RealmHelper.saveObjects(response.simulationRequestType)

                    RealmHelper.realm?.where(Country::class.java)?.findAll()?.let {
                        val temp = ArrayList<RealmObject>()
                        temp.addAll(it)
                        RealmHelper.remove(temp)
                    }
                    RealmHelper.saveObjects(response.countryList)

                    RealmHelper.realm?.where(Language::class.java)?.findAll()?.let {
                        val temp = ArrayList<RealmObject>()
                        temp.addAll(it)
                        RealmHelper.remove(temp)
                    }
                    RealmHelper.saveObjects(response.languageList)
                    RealmHelper.realm?.where(Territory::class.java)?.findAll()?.let {
                        val temp = ArrayList<RealmObject>()
                        temp.addAll(it)
                        RealmHelper.remove(temp)
                    }
                    RealmHelper.saveObjects(response.territory)

                    facebookLink = response.facebookUrl
                    instagramLink = response.instagramUrl
                    twitterLink = response.twitterUrl
                    takeAPhotoVideoLink = response.takePhotoVideoLink
                    view.listsLoaded()
                }

                override fun noInternet() {
                    view.apiError(API_INTERNET_ERROR) { getLists() }
                }

                override fun apiException(apiException: Base) {
                    view.apiError(API_SERVER_ERROR, apiException, null)
                }
            })
    }

    override fun getListsAfterLogin() {
        apiService.getListsAfterLogin()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .doOnSubscribe { view.startLoading() }
            .doOnSuccess { view.stopLoading() }
            .doOnError { view.stopLoading() }
            .subscribe(object : ResponseHandler<BaseListAfterLogin>() {
                override fun onSuccess(response: BaseListAfterLogin) {
                    RealmHelper.realm?.where(Procedure::class.java)?.findAll()?.let {
                        val temp = ArrayList<RealmObject>()
                        temp.addAll(it)
                        RealmHelper.remove(temp)
                    }
                    RealmHelper.saveObjects(response.procedureList)

                    RealmHelper.realm?.where(Clinic::class.java)?.findAll()?.let {
                        val temp = ArrayList<RealmObject>()
                        temp.addAll(it)
                        RealmHelper.remove(temp)
                    }
                    RealmHelper.saveObjects(response.clinicList)

                    RealmHelper.realm?.where(HairLossType::class.java)?.findAll()?.let {
                        val temp = ArrayList<RealmObject>()
                        temp.addAll(it)
                        RealmHelper.remove(temp)
                    }
                    RealmHelper.saveObjects(response.hairLossTypeList)

                    RealmHelper.realm?.where(HairType::class.java)?.findAll()?.let {
                        val temp = ArrayList<RealmObject>()
                        temp.addAll(it)
                        RealmHelper.remove(temp)
                    }
                    RealmHelper.saveObjects(response.hairTypeList)

                    view.listsAfterLoginLoaded()
                }

                override fun noInternet() {
                    view.apiError(API_INTERNET_ERROR) { getListsAfterLogin() }
                }

                override fun apiException(apiException: Base) {
                    view.apiError(API_SERVER_ERROR, apiException, null)
                }
            })
    }

    override fun uploadImages(fileList: ArrayList<File>) {
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
        fileList.forEachIndexed { index, file ->
            requestBody.addFormDataPart(
                "images[$index]",
                "file$index.jpg",
                RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
            )
        }


        apiService.uploadImages(requestBody.build())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .doOnSubscribe { view.startLoading() }
            .doOnSuccess { view.stopLoading() }
            .doOnError { view.stopLoading() }
            .subscribe(object : ResponseHandler<ArrayList<String>>() {
                override fun onSuccess(response: ArrayList<String>) {
                    view.imageListUploaded(response)
                }

                override fun noInternet() {
                    view.apiError(API_INTERNET_ERROR) { uploadImages(fileList) }
                }

                override fun apiException(apiException: Base) {
                    view.apiError(API_SERVER_ERROR, apiException, null)
                }
            })
    }

    override fun getGeocodeByAutoComplete(input: String) {
//        apiService.getGeocodeByAutoComplete(input)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .unsubscribeOn(Schedulers.io())
//            .doOnSubscribe { view.startLoading() }
//            .doOnSuccess { view.stopLoading() }
//            .doOnError { view.stopLoading() }
//            .subscribe(object : ResponseHandler<BaseList>() {
//                override fun onSuccess(response: BaseList) {
//                    view.geocodeListLoaded(response)
//                }
//
//                override fun noInternet() {
//                    view.apiError(API_INTERNET_ERROR, { getGeocodeByAutoComplete(input) })
//                }
//
//                override fun apiException(apiException: Base) {
//                    view.apiError(API_SERVER_ERROR, null, apiException)
//                }
//            })
    }

    override fun getAddressByLatLng(lat: Double, lng: Double) {
//        apiService.getAddressByLatLng("$lat,$lng")
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .unsubscribeOn(Schedulers.io())
//            .doOnSubscribe { view.startLoading() }
//            .doOnSuccess { view.stopLoading() }
//            .doOnError { view.stopLoading() }
//            .subscribe(object : ResponseHandler<BaseList>() {
//                override fun onSuccess(response: BaseList) {
//                    view.geocodeListLoaded(response)
//                }
//
//                override fun noInternet() {
//                    view.apiError(API_INTERNET_ERROR, { getAddressByLatLng(lat, lng) })
//                }
//
//                override fun apiException(apiException: Base) {
//                    view.apiError(API_SERVER_ERROR, null, apiException)
//                }
//            })
    }

    override fun getLocalization(languageKey: String) {
        val lastTimestamp =
            RealmHelper.realm?.where(LastTimestamp::class.java)?.findFirst()?.timestamp ?: 1
        apiService.getLocalisation(languageKey, lastTimestamp)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .doOnSubscribe { view.startLoading() }
            .doOnSuccess { view.stopLoading() }
            .doOnError { view.stopLoading() }
            .subscribe(object : ResponseHandler<BaseLocalization>() {
                override fun onSuccess(response: BaseLocalization) {
                    if (response.localizationList.size > 0) {
                        LastTimestamp().apply { timestamp = response.lastTimestamp }.also {
                            RealmHelper.saveObject(it)
                        }
                        RealmHelper.realm?.where(Localization::class.java)?.findAll()?.let {
                            val temp = ArrayList<RealmObject>()
                            temp.addAll(it)
                            RealmHelper.remove(temp)
                        }
                        RealmHelper.saveObjects(response.localizationList)
                    }

                    view.localizationsLoaded()
                }

                override fun noInternet() {
                    view.apiError(API_INTERNET_ERROR) { getLocalization(languageKey) }
                }

                override fun apiException(apiException: Base) {
                    view.apiError(API_SERVER_ERROR, apiException, null)
                }
            })
    }
}
