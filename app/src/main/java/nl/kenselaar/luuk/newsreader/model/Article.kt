package nl.kenselaar.luuk.newsreader.model

import java.io.Serializable

data class Article(val Id: Int, val Title: String, val Summary: String, val PublishDate: String, val Image: String, val Url: String) : Serializable