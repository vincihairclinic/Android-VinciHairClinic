package com.example.vinciandroidv2.network

import com.example.vinciandroidv2.network.respons.*
import io.reactivex.Single
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    /** system api **/

    @GET("list")
    fun getLists(): Single<BaseList>

    @GET("list-after-login")
    fun getListsAfterLogin(): Single<BaseListAfterLogin>

    @POST("upload/images")
    @Streaming
    fun uploadImages(@Body file: RequestBody): Single<ArrayList<String>>

    @GET("localization")
    fun getLocalisation(
        @Query("language_key") languageKey: String,
        @Query("last_timestamp") lastTimeStamp: Long,
    ): Single<BaseLocalization>


    /** auth api **/
    @POST("auth/auto-register")
    @FormUrlEncoded
    fun autoRegister(
        @Field("country_id") countryId: Int,
        @Field("language_key") languageKey: String,
        @Field("app_state") appState: String
    ): Single<Token>

    @POST("auth/login")
    @FormUrlEncoded
    fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Single<Token>

    @POST("auth/register")
    fun register(@Body user: User): Single<Token>

    @POST("auth/forgot-password")
    @FormUrlEncoded
    fun forgotPassword(@Field("email") email: String): Single<Any>

    @POST("auth/change-password")
    @FormUrlEncoded
    fun changePassword(
        @Field("current_password") currentPassword: String,
        @Field("new_password") newPassword: String
    ): Single<Any>


    /** user api **/

    @GET("user")
    fun getUser(): Single<BaseUser>

    @POST("user/edit")
    fun editUser(@Body user: User): Single<Any>

    @POST("user/edit")
    fun editUserProcedureIdList(@Body userProcedureIdList: UserProcedureIdList): Single<Any>

    @POST("user/simulation-requests-create")
    fun simulationRequestsCreate(@Body simulation: Simulation): Single<Any>

    @POST("user/delete-account")
    fun deleteAccount(): Single<Any>


    /** home api **/

    @GET("home")
    fun getHome(): Single<BaseHome>


    /** product api **/

    @GET("product/{id}")
    fun getProductById(@Path("id") productId: Int): Single<BaseProduct>

    @GET("product/list")
    fun getProductList(@Query("page") page: Int): Single<BaseProduct>

    @GET("product/list/{id}")
    fun getProductListByCategory(
        @Path("id") categoryId: Int,
        @Query("page") page: Int
    ): Single<BaseProduct>

    @GET("product/list-categories")
    fun getProductCategoryList(): Single<BaseProduct>


    /** article api **/

    @GET("article/{id}")
    fun getArticleById(@Path("id") articleId: Int): Single<BaseArticle>

    @GET("article/list")
    fun getArticleList(@Query("page") page: Int): Single<BaseArticle>

    @GET("article/list/")
    fun getArticleListByCategory(
        @Query("page") page: Int
    ): Single<BaseArticle>

    @GET("podcast/list")
    fun getPodcastList(
        @Query("page") page: Int
    ): Single<BaseArticle>

    @GET("video/list")
    fun getYoutubeList(
        @Query("page") page: Int
    ): Single<BaseArticle>


    @GET("article/list-categories")
    fun getArticleCategoryList(): Single<BaseArticle>

    @GET("article/next-list/{id}")
    fun getNextArticleListAfterArticleId(
        @Path("id") currentArticleId: Int,
        @Query("page") page: Int
    ): Single<BaseArticle>

    @GET("podcast/{id}")
    fun getPodcastByID(
        @Path("id") currentArticleId: Int
    ): Single<BaseArticle>

    @GET("youtube/{id}")
    fun getYoutubeByID(
        @Path("id") currentArticleId: Int
    ): Single<BaseArticle>



}