package nl.kenselaar.luuk.newsreader.service

import nl.kenselaar.luuk.newsreader.model.ArticleResult
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("Articles")
    fun articles(): Call<ArticleResult>
}