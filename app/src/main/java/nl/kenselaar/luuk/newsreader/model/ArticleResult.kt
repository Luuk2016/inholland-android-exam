package nl.kenselaar.luuk.newsreader.model

import com.squareup.moshi.Json
import java.io.Serializable

@Json(name = "Results")
data class ArticleResult(val Results: List<Article>) : Serializable