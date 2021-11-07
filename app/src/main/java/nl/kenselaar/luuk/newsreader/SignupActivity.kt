package nl.kenselaar.luuk.newsreader

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import nl.kenselaar.luuk.newsreader.model.RegisterResponse
import nl.kenselaar.luuk.newsreader.model.User
import nl.kenselaar.luuk.newsreader.preferences.AppPreferences
import nl.kenselaar.luuk.newsreader.service.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        supportActionBar!!.title = applicationContext.getString(R.string.signup)

        AppPreferences.init(this)

        // Set the fields
        val usernameField = findViewById<EditText>(R.id.usernameField)
        val passwordField = findViewById<EditText>(R.id.passwordField)

        // Set the buttons
        val signupButton = findViewById<Button>(R.id.signupButton)
        val loginButton = findViewById<Button>(R.id.loginButton)

        loginButton.setOnClickListener {
            startActivity(Intent(this, AccountActivity::class.java))
        }

        signupButton.setOnClickListener {
            // Set values from fields
            val username = usernameField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (username.isEmpty()) {
                usernameField.error = applicationContext.getString(R.string.username_required_textfield)
                usernameField.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                passwordField.error = applicationContext.getString(R.string.password_required_textfield)
                passwordField.requestFocus()
                return@setOnClickListener
            }

            val retrofit = Retrofit.Builder()
                .baseUrl("https://inhollandbackend.azurewebsites.net/api/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

            val service = retrofit.create(ApiService::class.java)

            service.userRegister(User(username, password)).enqueue(object: Callback<RegisterResponse> {
                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, applicationContext.getString(R.string.failed_signup_toast), Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                    if (response.body() != null) {
                        if (response.body()?.Success == true) {
                            Toast.makeText(applicationContext, response.body()?.Message, Toast.LENGTH_SHORT).show()

                            // Open new page
                            startActivity(Intent(this@SignupActivity, AccountActivity::class.java))
                        } else {
                            Toast.makeText(applicationContext, response.body()?.Message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
        }
    }
}