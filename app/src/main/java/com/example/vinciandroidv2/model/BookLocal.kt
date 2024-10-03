package com.example.vinciandroidv2.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class BookLocal : RealmObject() {
    @PrimaryKey
    var id: Int = 0
    var line1: String? = null
    var line2: String? = null
    var image: Int? = null
    var bookPath1: String? = null
    var bookPath2: String? = null
    var isFavorite: Boolean = false
    var isOpened: Boolean = false
}