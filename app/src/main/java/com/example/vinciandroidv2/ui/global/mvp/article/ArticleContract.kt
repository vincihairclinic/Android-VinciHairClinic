package com.example.vinciandroidv2.ui.global.mvp.article

import com.example.vinciandroidv2.network.respons.BaseArticle
import com.example.vinciandroidv2.network.respons.VideoTag
import com.example.vinciandroidv2.network.respons.Youtube
import com.example.vinciandroidv2.ui.global.mvp.BaseView

interface ArticleContract {
    interface View : BaseView {
        fun articleByIdLoaded(baseArticle: BaseArticle) {}
        fun articleListByCategoryLoaded(baseArticle: BaseArticle) {}
        fun articleCategoryListLoaded(baseArticle: BaseArticle) {}
        fun nextArticleListLoaded(baseArticle: BaseArticle) {}
        fun showVideos(videos: ArrayList<Youtube>){}

    }

    interface Presenter {
        fun getArticleById(articleId: Int)
        fun getArticleList(page: Int)
        fun getArticleListByCategory( page: Int)
        fun getArticleCategoryList()
        fun getNextArticleListAfterArticleId(currentArticleId: Int, page: Int)
        fun getPodcastList( page: Int)
        fun getPodcastByID( id: Int)
        fun getYoutubeList(page: Int)
    }
}