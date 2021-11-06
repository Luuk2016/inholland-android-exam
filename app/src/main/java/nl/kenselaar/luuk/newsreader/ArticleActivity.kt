package nl.kenselaar.luuk.newsreader

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import coil.load
import coil.transform.CircleCropTransformation
import nl.kenselaar.luuk.newsreader.model.Article
import nl.kenselaar.luuk.newsreader.preferences.AppPreferences
import nl.kenselaar.luuk.newsreader.service.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ArticleActivity : AppCompatActivity() {
    companion object {
        const val ARTICLE = "article"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.article_view)
        supportActionBar!!.title = "Article"
        AppPreferences.init(this)

        val article = intent.getSerializableExtra(ARTICLE) as? Article
        Log.i("Detail", "Open article with id ${article?.Id}")

        val image = findViewById<ImageView>(R.id.Image)
        image.load(article?.Image) {
            crossfade(true)
            placeholder(R.drawable.ic_baseline_image_24)
            transformations(CircleCropTransformation())
        }

        val nameTextView = findViewById<TextView>(R.id.Title)
        nameTextView.text = article?.Title

        val summaryTextView = findViewById<TextView>(R.id.Summary)
        summaryTextView.text = article?.Summary

        val openInBrowserButton: Button = findViewById(R.id.openInBrowser)
        openInBrowserButton.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW, Uri.parse(article?.Url))
            startActivity(i)
        }

        val likeArticleButton: Button = findViewById(R.id.likeArticle)

        if (article != null) {
            if (!article.IsLiked) {
                likeArticleButton.text = "Like article"
            } else {
                likeArticleButton.text = "Unlike article"
            }
        }

        likeArticleButton.setOnClickListener {
            if (AppPreferences.isLogin) {
                if (article?.Id != null) {
                    val retrofit = Retrofit.Builder()
                        .baseUrl("https://inhollandbackend.azurewebsites.net/api/")
                        .addConverterFactory(MoshiConverterFactory.create())
                        .build()

                    val service = retrofit.create(ApiService::class.java)

                    if (!article.IsLiked) {
                        service.likeArticle(AppPreferences.authToken, article.Id).enqueue(object: Callback<Void>{
                            override fun onFailure(call: Call<Void>, t: Throwable) {
                                Toast.makeText(applicationContext, "Could not like article, please try again", Toast.LENGTH_SHORT).show()
                            }

                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                if (response.code() == 200) {
                                    Toast.makeText(applicationContext, "Article has been liked", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(applicationContext, "Could not like article, please try again", Toast.LENGTH_SHORT).show()
                                }
                            }
                        })
                    } else {
                        service.unlikeArticle(AppPreferences.authToken, article.Id).enqueue(object: Callback<Void>{
                            override fun onFailure(call: Call<Void>, t: Throwable) {
                                Toast.makeText(applicationContext, "Could not unlike article, please try again", Toast.LENGTH_SHORT).show()
                            }

                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                if (response.code() == 200) {
                                    Toast.makeText(applicationContext, "Article has been unliked", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(applicationContext, "Could not unlike article, please try again", Toast.LENGTH_SHORT).show()
                                }
                            }
                        })
                    }
                }
            } else {
                Toast.makeText(applicationContext, "Please login first.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}