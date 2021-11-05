package nl.kenselaar.luuk.newsreader

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
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

        R.id.account -> {
            startActivity(Intent(this, AccountActivity::class.java))
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

        var count = 20
        getArticles(count)

        val loadMoreButton: Button = findViewById(R.id.getMore)
        loadMoreButton.isVisible = false
        loadMoreButton.setOnClickListener {
            count += 20
            getArticles(count)
        }
    }

    private fun getArticles(count: Int) {
        AppPreferences.init(this)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://inhollandbackend.azurewebsites.net/api/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        val service = retrofit.create(ApiService::class.java)

        if (AppPreferences.isLogin) {
            service.getArticlesAuthenticated(AppPreferences.authToken, count).enqueue(this)
        } else {
            service.getArticles(count).enqueue(this)
        }
    }

    override fun onResponse(call: Call<ArticleResult>, response: Response<ArticleResult>) {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = MyAdapter(response.body()!!.Results)
        adapter.setItemListener(this)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val loadingArticles: TextView = findViewById(R.id.loading_articles)
        loadingArticles.isVisible = false

        val loadMoreButton: Button = findViewById(R.id.getMore)
        loadMoreButton.isVisible = true
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