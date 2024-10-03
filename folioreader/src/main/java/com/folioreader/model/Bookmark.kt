package com.folioreader.model

import io.realm.RealmObject

open class Bookmark(
    var bookId: String? = null,
    var locatorJson: String? = null,
    var createdAt: String? = null
) : RealmObject()