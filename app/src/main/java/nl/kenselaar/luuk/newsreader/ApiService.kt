package nl.kenselaar.luuk.newsreader

import nl.kenselaar.luuk.newsreader.model.Article
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("Articles")
    fun articles(): Call<List<Article>>
}