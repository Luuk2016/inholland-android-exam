package nl.kenselaar.luuk.newsreader

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class AccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        supportActionBar!!.title = "Login"

        var loginButton = findViewById<Button>(R.id.loginButton)
        loginButton.setOnClickListener {
            val toast = Toast.makeText(applicationContext, "Login clicked!", Toast.LENGTH_SHORT)
            toast.show()
        }

        var signupButton = findViewById<Button>(R.id.signupButton)
        signupButton.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }
}