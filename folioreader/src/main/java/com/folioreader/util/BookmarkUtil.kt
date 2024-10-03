package com.folioreader.util

import com.folioreader.model.Bookmark
import com.folioreader.model.FolioModule
import io.realm.Realm
import io.realm.RealmConfiguration

class BookmarkUtil {
    val realm: Realm by lazy { Realm.getInstance(mRealmConfiguration) }
    var listener: BookmarkCallback? = null

    private val mRealmConfiguration = RealmConfiguration.Builder()
        .schemaVersion(9)
        .modules(FolioModule())
        .deleteRealmIfMigrationNeeded()
        .build()

    fun getBookmarks() {
        val list = ArrayList<Bookmark>()
        realm.where(Bookmark::class.java)?.findAll()?.let {
            list.addAll(it)
        }
        listener?.bookmarkResult(list)
    }

    fun removeBookmark(bookmark: Bookmark) {
        realm.executeTransaction {
            bookmark.deleteFromRealm()
            getBookmarks()
        }
    }

    interface BookmarkCallback {
        fun bookmarkResult(list: ArrayList<Bookmark>)
    }
}