package com.example.vinciandroidv2.network.respons

import com.google.gson.annotations.SerializedName
import io.realm.annotations.PrimaryKey

class BaseProduct {
    @SerializedName("product")
    var product: Product? = null

    @SerializedName("products")
    var productList: ArrayList<Product>? = null

    @SerializedName("product_categories")
    var productCategoryList: ArrayList<ProductCategory>? = null
}

class Product {
    @PrimaryKey
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("product_category_id")
    var productCategoryId: Int = 0

    @SerializedName("shop_now_urls")
    var shopNowUrlList: ArrayList<ShopNowUrl>? = null

    @SerializedName("sort")
    var sort: Long? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("url_images")
    var urlImageList: ArrayList<String>? = null

    @SerializedName("about")
    var aboutProduct: String? = null

    @SerializedName("faqs")
    var faqs: ArrayList<FAQ>? = null

    @SerializedName("url_video")
    var urlVideo: String? = null

    @SerializedName("video_name")
    var videoName: String? = null

    @SerializedName("product_reviews")
    var productReviewList: ArrayList<ProductReview>? = null

    @SerializedName("product_category")
    var productCategory: ProductCategory? = null
}

class ShopNowUrl {
    @PrimaryKey
    @SerializedName("country_id")
    var countryId: Int = 0

    @SerializedName("shop_now_url")
    var shopNowUrl: String? = null
}

class FAQ {
    @SerializedName("question")
    var question: String? = null

    @SerializedName("answer")
    var answer: String? = null
}

class ProductReview {
    @PrimaryKey
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("product_id")
    var productId: Int = 0

    @SerializedName("name")
    var name: String? = null

    @SerializedName("url_video")
    var urlVideo: String? = null
}

class ProductCategory {
    @PrimaryKey
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("name")
    var name: String? = null

    @SerializedName("name_en")
    var nameEn: String? = null

    @SerializedName("name_pt")
    var namePt: String? = null

    @SerializedName("sort")
    var sort: Long? = null
}