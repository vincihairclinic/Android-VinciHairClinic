package com.example.vinciandroidv2.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class BookLocator : RealmObject() {
    @PrimaryKey
    var bookId: String? = null
    var locatorJson: String? = null
}