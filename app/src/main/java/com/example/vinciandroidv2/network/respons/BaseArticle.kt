package com.example.vinciandroidv2.network.respons

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

class BaseArticle {
    @SerializedName("article")
    var article: Article? = null

    @SerializedName("articles")
    var articleList: ArrayList<Article>? = null

    @SerializedName("article_categories")
    var articleCategoryList: ArrayList<ArticleCategory>? = null

    @SerializedName("podcast_episodes")
    var podcasts: ArrayList<PodcastEpisode>? = null

    @SerializedName("podcast")
    var podcast: Podcast? = null

    @SerializedName("videos")
    var videos: ArrayList<Youtube> = ArrayList()
}

enum class ArticleType { ARTICLE, SUBSCRIBE_SOCIALS }
class Podcast {
    @PrimaryKey
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("created_at")
    var createdAt: String? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("url_image")
    var urlImage: String? = null

    @SerializedName("content")
    var content: String? = null

    @SerializedName("podcast_episodes")
    var podcastsEpisodes: ArrayList<PodcastEpisode>? = null
}

class PodcastEpisode {
    @PrimaryKey
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("duration_min")
    var durationMin: Int = 0

    @SerializedName("created_at")
    var createdAt: String? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("url_image")
    var urlImage: String? = null

    @SerializedName("url_file")
    var urlFile: String? = null

    @SerializedName("content")
    var content: String? = null
}

class Youtube {
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("title")
    var title: String? = null

    @SerializedName("is_live_stream")
    var isLiveStream: String? = null

    @SerializedName("author_name")
    var authorName: String? = null

    @SerializedName("content")
    var content: String? = null

    @SerializedName("published_at")
    var publishedAt: String? = null

    @SerializedName("votes_count")
    var votesCount: Int? = null

    @SerializedName("url_preview")
    var urlPreview: String? = null

    @SerializedName("url_source")
    var urlSource: String? = null

    @SerializedName("url_author_image")
    var urlAuthorImage: String? = null

    @SerializedName("source")
    var source: String? = null

}

class VideoTag {
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("name")
    var name: String? = null
}

class Article {
    @PrimaryKey
    @SerializedName("id")
    var id: Int = 0


    @SerializedName("name")
    var name: String? = null

    @SerializedName("url_image")
    var urlImage: String? = null

    @SerializedName("content")
    var content: String? = null

    @SerializedName("created_at")
    var createdAt: String? = null

    @SerializedName("article_category")
    var articleCategory: ArticleCategory? = null

    var articleType = ArticleType.ARTICLE
}

class ArticleCategory {
    @PrimaryKey
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("name")
    var name: String? = null
}