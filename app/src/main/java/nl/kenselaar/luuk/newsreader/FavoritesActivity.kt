package nl.kenselaar.luuk.newsreader

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import nl.kenselaar.luuk.newsreader.model.Article
import nl.kenselaar.luuk.newsreader.model.ArticleResult
import nl.kenselaar.luuk.newsreader.preferences.AppPreferences
import nl.kenselaar.luuk.newsreader.service.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class FavoritesActivity : AppCompatActivity(), Callback<ArticleResult>, MyItemListener  {

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        AppPreferences.init(this)
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.favorites_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.refresh -> {
            getLikedArticles()
            true
        } else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.favorites_view)
        supportActionBar!!.title = "Favorite articles"
        AppPreferences.init(this)

        getLikedArticles()
    }

    private fun getLikedArticles() {
        AppPreferences.init(this)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://inhollandbackend.azurewebsites.net/api/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        val service = retrofit.create(ApiService::class.java)
        service.getLikedArticles(AppPreferences.authToken).enqueue(this)
    }

    override fun onResponse(call: Call<ArticleResult>, response: Response<ArticleResult>) {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = MyAdapter(response.body()!!.Results)
        adapter.setItemListener(this)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val loadingArticles: TextView = findViewById(R.id.loading_articles)
        loadingArticles.isVisible = false
    }

    override fun onFailure(call: Call<ArticleResult>, t: Throwable) {
        Log.i("Detail", t.message.toString())
    }

    override fun onItemClicked(item: Article) {
        val intent = Intent(this, ArticleActivity::class.java)
        intent.putExtra(ArticleActivity.ARTICLE, item)
        startActivity(intent)
    }
}