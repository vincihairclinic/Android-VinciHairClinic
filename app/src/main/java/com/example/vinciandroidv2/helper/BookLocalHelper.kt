package com.example.vinciandroidv2.helper

import android.content.Context
import com.example.vinciandroidv2.model.BookLocal

//val bookPathList = arrayListOf(
//    "file:///android_asset/ht_client.epub",
//    "file:///android_asset/coating_surveys.epub"
//)
//val bookNameList = arrayListOf(
//    "Introduction",
//    "Coating Surveys"
//)

enum class BookPath(
    val bookPath1: String,
    val bookPath2: String
) {
    HAIR_TRANSPLANT_CLIENT_INFORMATION(
        "HT_Client_English_rev1-2",
        "HT_Client_Portugues_rev1-2"
    ),
    MSP_CLIENT_INFORMATION(
        "MSP_Client_Information_eng",
        "MSP_Client_Information_portu"
    )
}

fun createBookList(context: Context?) {
    BookPath.values().forEachIndexed { index, book ->
        BookLocal().apply {
            id = index.plus(1)
//            image = context?.resources?.getIdentifier(
//                "chapter_${index.plus(1)}_v2_dark",
//                "drawable",
//                context.packageName
//            )
            bookPath1 = book.bookPath1
            bookPath2 = book.bookPath2

            RealmHelper.realm?.where(BookLocal::class.java)
                ?.findAll()
                ?.firstOrNull { it.id == index }
                .let {
                    isFavorite = it?.isFavorite ?: false
                    isOpened = it?.isOpened ?: false
                }
        }.also { RealmHelper.saveObject(it) }
    }
}

//fun createBookList(context: Context?) {
//    for (i in 1..2) {
//        BookLocal().apply {
//            id = i
////            line1 = "Chapter $i"
//            line2 = bookNameList[i - 1]
////            image = resources.getIdentifier("chapter_${i}_v2_dark", "drawable", this.packageName)
//            image = context?.resources?.getIdentifier(
//                "chapter_${i}_v2_dark",
//                "drawable",
//                context.packageName
//            )
//            bookPath = bookPathList[i - 1]
//            isFavorite = RealmHelper.realm?.where(BookLocal::class.java)
//                ?.findAll()
//                ?.firstOrNull { it.id == i }
//                ?.isFavorite ?: false
//            isOpened = RealmHelper.realm?.where(BookLocal::class.java)
//                ?.findAll()
//                ?.firstOrNull { it.id == i }
//                ?.isOpened ?: false
//        }.also {
//            RealmHelper.saveObject(it)
//        }
//    }
//}