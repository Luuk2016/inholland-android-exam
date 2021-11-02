package nl.kenselaar.luuk.newsreader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class FavoritesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.favorites_view)
        supportActionBar!!.title = "Favorite articles"

    }
}