package nl.kenselaar.luuk.newsreader

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import nl.kenselaar.luuk.newsreader.ArticleActivity.Companion.ARTICLE
import nl.kenselaar.luuk.newsreader.model.Article
import nl.kenselaar.luuk.newsreader.model.ArticleResult
import nl.kenselaar.luuk.newsreader.preferences.AppPreferences
import nl.kenselaar.luuk.newsreader.service.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : AppCompatActivity(), Callback<ArticleResult>, MyItemListener  {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://inhollandbackend.azurewebsites.net/api/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    private val service: ApiService = retrofit.create(ApiService::class.java)

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        AppPreferences.init(this)
        val inflater: MenuInflater = menuInflater

        if (AppPreferences.isLogin) {
            inflater.inflate(R.menu.main_menu_logged_in, menu)
        } else {
            inflater.inflate(R.menu.main_menu, menu)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.refresh -> {
            recreate()
            true
        }
        R.id.login -> {
            startActivity(Intent(this, AccountActivity::class.java))
            true
        }
        R.id.signup -> {
            startActivity(Intent(this, SignupActivity::class.java))
            true
        }
        R.id.logout -> {
            AppPreferences.init(this)
            AppPreferences.isLogin = false
            AppPreferences.authToken = ""
            Toast.makeText(applicationContext, "You have been logged out!", Toast.LENGTH_SHORT).show()
            recreate()
            true
        }
        R.id.favorites -> {
            AppPreferences.init(this)

            // Check if logged in
            if (AppPreferences.isLogin) {
                startActivity(Intent(this, FavoritesActivity::class.java))
            } else {
                Toast.makeText(applicationContext, "Please login first!", Toast.LENGTH_SHORT).show()
            }

            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_view)

        // Set the amount of articles to get
        var count = 20

        getArticles(count)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Load 20 articles when reaching the bottom
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    Toast.makeText(applicationContext, "Loading 20 more", Toast.LENGTH_SHORT).show()
                    count += 20
                    getArticles(count)
                }
            }
        })
    }

    private fun getArticles(count: Int) {
        AppPreferences.init(this)

        service.getArticles(AppPreferences.authToken.ifEmpty { null }, count).enqueue(this)
    }

    override fun onResponse(call: Call<ArticleResult>, response: Response<ArticleResult>) {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)

        val adapter = MyAdapter(response.body()!!.Results)
        adapter.setItemListener(this)

        recyclerView.adapter = adapter

        val loadingArticles: TextView = findViewById(R.id.loading_articles)
        loadingArticles.isVisible = false

        // Check if any articles have been found
        if (response.body()!!.Results.isEmpty()) {
            val noArticlesFound: TextView = findViewById(R.id.no_articles_found)
            noArticlesFound.isVisible = true
        }
    }

    override fun onFailure(call: Call<ArticleResult>, t: Throwable) {
        Log.i("Detail", t.message.toString())
    }

    override fun onItemClicked(item: Article) {
        val intent = Intent(this, ArticleActivity::class.java)
        intent.putExtra(ARTICLE, item)
        startActivity(intent)
    }
}