package com.example.vinciandroidv2.network.respons

import com.google.gson.annotations.SerializedName
import io.realm.annotations.PrimaryKey

class BaseHome {
    @SerializedName("article")
    var article: Article? = null

    @SerializedName("procedure_results")
    var procedureResultList: ArrayList<ProcedureResult>? = null

    @SerializedName("books")
    var bookList: ArrayList<BookInfo>? = null

    @SerializedName("podcast_episodes")
    var recommendedPodcasts: ArrayList<PodcastEpisode> = ArrayList()

    @SerializedName("articles")
    var recommendedArticles: ArrayList<Article> = ArrayList()
}

class ProcedureResult {
    @PrimaryKey
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("procedure_id")
    var procedureId: Int? = null
    @SerializedName("gender_id")
    var genderId: Int? = null
    @SerializedName("date")
    var date: String? = null

    @SerializedName("created_at")
    var createdAt: String? = null

    @SerializedName("title")
    var title: String? = null

    @SerializedName("subtitle")
    var subtitle: String? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("url_image")
    var urlImage: String? = null

    @SerializedName("content")
    var content: String? = null

    @SerializedName("procedure")
    var procedure: Procedure? = null

    @SerializedName("procedure_result_videos")
    var procedureResultVideoList: ArrayList<ProcedureResultVideo>? = null

    @SerializedName("procedure_result_images")
    var procedureResultImageList: ArrayList<ProcedureResultImage>? = null
}

class ProcedureResultVideo {
    @PrimaryKey
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("procedure_result_id")
    var procedureResultId: Int? = null

    @SerializedName("language_key")
    var languageKey: String? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("video")
    var video: String? = null

    @SerializedName("sort")
    var sort: Long? = null

    @SerializedName("created_at")
    var createdAt: String? = null

    @SerializedName("updated_at")
    var updatedAt: String? = null

    @SerializedName("url_video")
    var urlVideo: String? = null
}

class ProcedureResultImage {
    @PrimaryKey
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("procedure_result_id")
    var procedureResultId: Int? = null

    @SerializedName("language_key")
    var languageKey: String? = null

    @SerializedName("befor_image")
    var beforeImage: String? = null

    @SerializedName("after_image")
    var afterImage: String? = null

    @SerializedName("sort")
    var sort: Long? = null

    @SerializedName("created_at")
    var createdAt: String? = null

    @SerializedName("updated_at")
    var updatedAt: String? = null

    @SerializedName("url_befor_image")
    var urlBeforeImage: String? = null

    @SerializedName("url_after_image")
    var urlAfterImage: String? = null
}

class BookInfo {
    @PrimaryKey
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("name")
    var name: String? = null

    @SerializedName("url_image")
    var urlImage: String? = null

    @SerializedName("about")
    var about: String? = null

    @SerializedName("url_video")
    var urlVideo: String? = null

    @SerializedName("faqs")
    var faqList: ArrayList<FAQ>? = null

    @SerializedName("book_reviews")
    var bookReviewList: ArrayList<BookReview>? = null

    @SerializedName("book_informations")
    var bookInformationList: ArrayList<BookInformation>? = null


    /** PRE **/

    @SerializedName("pre_name")
    var preName: String? = null

    @SerializedName("pre_content")
    var preContent: String? = null

    @SerializedName("url_pre_image")
    var urlPreImage: String? = null

    @SerializedName("book_pre_instructions")
    var bookPreInstructionList: ArrayList<Instruction>? = null

    @SerializedName("book_pre_additionals")
    var bookPreAdditionalList: ArrayList<PreAdditionInfo>? = null


    /** POST **/

    @SerializedName("post_name")
    var postName: String? = null

    @SerializedName("post_content")
    var postContent: String? = null

    @SerializedName("url_post_image")
    var urlPostImage: String? = null

    @SerializedName("book_post_instructions")
    var bookPostInstructionList: ArrayList<Instruction>? = null

    @SerializedName("book_post_additionals")
    var bookPostAdditionalList: ArrayList<PostAdditionInfo>? = null
}

class BookReview {
    @PrimaryKey
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("book_id")
    var bookId: Int? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("url_video")
    var urlVideo: String? = null
}

class BookInformation {
    @PrimaryKey
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("book_id")
    var bookId: Int? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("content")
    var content: String? = null

    @SerializedName("url_image")
    var urlImage: String? = null
}

class Instruction {
    @SerializedName("title")
    var title: String? = null

    @SerializedName("content")
    var content: String? = null
}

class PreAdditionInfo {
    @SerializedName("title")
    var title: String? = null

    @SerializedName("content")
    var content: String? = null

    @SerializedName("url_images")
    var urlImageList: ArrayList<String>? = null

    @SerializedName("book_pre_additional_items")
    var additionItemList: ArrayList<AdditionItem>? = null
}

class PostAdditionInfo {
    @SerializedName("title")
    var title: String? = null

    @SerializedName("content")
    var content: String? = null

    @SerializedName("url_images")
    var urlImageList: ArrayList<String>? = null

    @SerializedName("book_post_additional_items")
    var additionItemList: ArrayList<AdditionItem>? = null
}

class AdditionItem {
    @SerializedName("title")
    var title: String? = null

    @SerializedName("content")
    var content: String? = null
}