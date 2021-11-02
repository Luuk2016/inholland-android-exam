package nl.kenselaar.luuk.newsreader

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import coil.load
import nl.kenselaar.luuk.newsreader.model.Article

class ArticleActivity : AppCompatActivity() {
    companion object {
        const val ARTICLE = "article"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.article_view)
        supportActionBar!!.title = "Article"

        val article = intent.getSerializableExtra(ARTICLE) as? Article
        Log.i("Detail", "Open article with id ${article?.Id}")

        val image = findViewById<ImageView>(R.id.Image)
        image.load(article?.Image)

        val nameTextView = findViewById<TextView>(R.id.Title)
        nameTextView.text = article?.Title

        val summaryTextView = findViewById<TextView>(R.id.Summary)
        summaryTextView.text = article?.Summary

        val button: Button = findViewById(R.id.openInBrowser)
        button.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW, Uri.parse(article?.Url))
            startActivity(i)
        }
    }
}