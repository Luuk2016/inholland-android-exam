package nl.kenselaar.luuk.newsreader.service

import nl.kenselaar.luuk.newsreader.model.ArticleResult
import nl.kenselaar.luuk.newsreader.model.LoginResponse
import nl.kenselaar.luuk.newsreader.model.RegisterResponse
import nl.kenselaar.luuk.newsreader.model.User
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("Articles")
    fun getArticles(): Call<ArticleResult>

    @GET("Articles")
    fun getArticlesAuthenticated(
        @Header("x-authtoken") authHeader: String?
    ): Call<ArticleResult>

    @GET("Articles/liked")
    fun getLikedArticles(
        @Header("x-authtoken") authHeader: String?
    ): Call<ArticleResult>

    @PUT("Articles/{id}/like")
    fun likeArticle(
        @Path("id") id: Int?,
        @Header("x-authtoken") authHeader: String?
    ): Call<Void>

    @DELETE("Articles/{id}/like")
    fun unlikeArticle(
        @Path("id") id: Int?,
        @Header("x-authtoken") authHeader: String?
    ): Call<Void>

    @Headers("Content-Type: application/json")
    @POST("Users/login")
    fun userLogin(@Body body: User) : Call<LoginResponse>

    @Headers("Content-Type: application/json")
    @POST("Users/register")
    fun userRegister(@Body body: User) : Call<RegisterResponse>
}