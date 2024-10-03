package com.example.vinciandroidv2.helper

import com.example.vinciandroidv2.model.BookLocal
import com.example.vinciandroidv2.network.respons.*
import io.realm.Realm
import io.realm.RealmObject
import com.example.vinciandroidv2.ui.auth.questionnaire.QuestionnaireFlow
import com.example.vinciandroidv2.ui.auth.questionnaire.questionnaireFlow
import com.example.vinciandroidv2.ui.global.defaultHost
import com.example.vinciandroidv2.ui.global.selectedCountryId
import com.example.vinciandroidv2.ui.global.selectedLanguageKey
import com.example.vinciandroidv2.ui.global.userToken

object RealmHelper {
    var realm: Realm? = Realm.getDefaultInstance()

    fun <E : RealmObject> saveObject(obj: E) {
        if (true == realm?.isInTransaction) {
            realm?.insertOrUpdate(obj)
        } else {
            realm?.executeTransaction { nRealm ->
                nRealm.insertOrUpdate(obj)
            }
        }
    }

    fun <E : RealmObject> saveObjects(objects: ArrayList<E>) {
        if (true == realm?.isInTransaction) {
            realm?.insertOrUpdate(objects)
        } else {
            realm?.executeTransaction { nRealm ->
                nRealm.insertOrUpdate(objects)
            }
        }
    }

    fun <E : RealmObject> remove(objects: ArrayList<E>) {
        objects.forEach { obj ->
            if (true == realm?.isInTransaction) {
                obj.deleteFromRealm()
            } else {
                realm?.executeTransaction {
                    obj.deleteFromRealm()
                }
            }
        }
    }

    fun <E : RealmObject> E.copyFromRealm(): E? {
        var obj: E? = null

        if (true == realm?.isInTransaction) {
            obj = realm?.copyFromRealm(this)
        } else {
            realm?.executeTransaction {
                obj = realm?.copyFromRealm(this)
            }
        }

        return obj
    }

    fun getHostFromSelectedCountry(countryId: Int): String = realm?.where(Country::class.java)
        ?.equalTo("id", countryId)
        ?.findFirst()
        ?.host
        ?: defaultHost

    fun getUserData(): User? = realm?.where(User::class.java)?.findFirst()
    fun getLocalizedText(key: String): String =
        realm?.where(Localization::class.java)?.equalTo("key", key)?.findFirst()?.value ?: key

    fun removeSensitiveDataFromBase() {
        realm?.where(Token::class.java)?.findAll()?.forEach { token ->
            remove(arrayListOf(token))
        }
        realm?.where(User::class.java)?.findAll()?.forEach { user ->
            remove(arrayListOf(user))
        }
        realm?.where(UserFaceId::class.java)?.findAll()?.forEach { user ->
            remove(arrayListOf(user))
        }
        userToken = null
        selectedCountryId = -1
        selectedLanguageKey = ""
        com.example.vinciandroidv2.ui.global.host = defaultHost

        questionnaireFlow = QuestionnaireFlow.AUTH
    }

    fun getClinicById(clinicId: Int): Clinic? =
        realm?.where(Clinic::class.java)?.equalTo("id", clinicId)?.findFirst()

    fun getCountryById(countryId: Int): Country? =
        realm?.where(Country::class.java)?.equalTo("id", countryId)?.findFirst()

    fun removeLastTimeStamp() {
        realm?.where(LastTimestamp::class.java)?.findAll()?.let {
            val temp = ArrayList<RealmObject>()
            temp.addAll(it)
            remove(temp)
        }
    }

    fun getOpenedBook(): BookLocal? =
        realm?.where(BookLocal::class.java)?.equalTo("isOpened", true)?.findFirst()

    fun getBookById(bookId: Int?): BookLocal? =
        realm?.where(BookLocal::class.java)?.equalTo("id", bookId)?.findFirst()

    fun getBookByPath(bookPath: String): BookLocal? =
        realm?.where(BookLocal::class.java)?.equalTo("bookPath", bookPath)?.findFirst()
}