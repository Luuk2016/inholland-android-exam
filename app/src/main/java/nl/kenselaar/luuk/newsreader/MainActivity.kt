package nl.kenselaar.luuk.newsreader

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import nl.kenselaar.luuk.newsreader.ArticleActivity.Companion.ARTICLE
import nl.kenselaar.luuk.newsreader.model.Article
import nl.kenselaar.luuk.newsreader.model.ArticleResult
import nl.kenselaar.luuk.newsreader.service.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : AppCompatActivity(), Callback<ArticleResult>, MyItemListener  {

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
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
        R.id.favorites -> {
            // @TODO: CHECK IF LOGGEDIN
            if (true) {
                startActivity(Intent(this, FavoritesActivity::class.java))
            } else {
                val toast = Toast.makeText(applicationContext, "Please login first!", Toast.LENGTH_SHORT)
                toast.show()
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

        val retrofit = Retrofit.Builder()
            .baseUrl("https://inhollandbackend.azurewebsites.net/api/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        val service = retrofit.create(ApiService::class.java)
        service.articles().enqueue(this)
    }

    override fun onResponse(call: Call<ArticleResult>, response: Response<ArticleResult>) {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = MyAdapter(response.body()!!.Results)
        adapter.setItemListener(this)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
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