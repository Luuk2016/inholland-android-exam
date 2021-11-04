package nl.kenselaar.luuk.newsreader

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import nl.kenselaar.luuk.newsreader.model.LoginResponse
import nl.kenselaar.luuk.newsreader.model.User
import nl.kenselaar.luuk.newsreader.preferences.AppPreferences
import nl.kenselaar.luuk.newsreader.service.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class AccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        supportActionBar!!.title = "Login"

        AppPreferences.init(this)

        // Set the fields
        val usernameField = findViewById<EditText>(R.id.usernameField)
        val passwordField = findViewById<EditText>(R.id.passwordField)

        // Set the buttons
        val signupButton = findViewById<Button>(R.id.signupButton)
        val loginButton = findViewById<Button>(R.id.loginButton)

        signupButton.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        loginButton.setOnClickListener {
            // Set values from fields
            val username = usernameField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (username.isEmpty()) {
                usernameField.error = "Username is required"
                usernameField.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                passwordField.error = "Password is required"
                passwordField.requestFocus()
                return@setOnClickListener
            }

            val retrofit = Retrofit.Builder()
                .baseUrl("https://inhollandbackend.azurewebsites.net/api/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

            val service = retrofit.create(ApiService::class.java)

            service.userLogin(User(username, password)).enqueue(object: Callback<LoginResponse>{
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, "Login failed, please try again.", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.body() != null) {
                        Toast.makeText(applicationContext, response.body()?.AuthToken, Toast.LENGTH_SHORT).show()
                        AppPreferences.isLogin = true
                        AppPreferences.authToken = response.body()?.AuthToken.toString()
                    } else {
                        Toast.makeText(applicationContext, "Failed to login!", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }
}
