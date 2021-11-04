package nl.kenselaar.luuk.newsreader.service

import nl.kenselaar.luuk.newsreader.model.ArticleResult
import nl.kenselaar.luuk.newsreader.model.LoginResponse
import nl.kenselaar.luuk.newsreader.model.User
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("Articles")
    fun articles(): Call<ArticleResult>

    @Headers("Content-Type: application/json")
    @POST("Users/login")
    fun userLogin(@Body body: User) : Call<LoginResponse>
}