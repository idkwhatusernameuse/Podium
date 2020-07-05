package dev.idkwuu.allesandroid

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import dev.idkwuu.allesandroid.api.AllesEndpointsInterface
import dev.idkwuu.allesandroid.api.RetrofitClientInstance
import dev.idkwuu.allesandroid.models.LegacyToken
import dev.idkwuu.allesandroid.models.LegacyUserCredentials
import dev.idkwuu.allesandroid.util.SharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        findViewById<Button>(R.id.login_button).setOnClickListener {
            tryToLogIn()
        }
    }

    private fun tryToLogIn() {
        val username = findViewById<TextInputEditText>(R.id.textfield_username_input).text.toString()
        val password = findViewById<TextInputEditText>(R.id.textfield_password_input).text.toString()

        if (username.isNotEmpty() && password.isNotEmpty()) {
            // Show loading screen
            val loading = findViewById<ConstraintLayout>(R.id.loading)
            loading.visibility = View.VISIBLE
            // Login shit with Retrofit
            val retrofit = RetrofitClientInstance().getRetrofitInstance()
                .create(AllesEndpointsInterface::class.java)
            val credentials = LegacyUserCredentials()
            credentials.username = username
            credentials.password = password
            val call = retrofit.getToken(credentials)
            call!!.enqueue(object : Callback<LegacyToken?> {
                override fun onFailure(call: Call<LegacyToken?>, t: Throwable) {
                    loading.visibility = View.GONE
                    TODO("Not yet implemented")
                }

                override fun onResponse(call: Call<LegacyToken?>, response: Response<LegacyToken?>) {
                    if (response.body()?.token != null) {
                        SharedPreferences.login_token = response.body()?.token
                        SharedPreferences.isLoggedIn = true
                        openMainActivity()
                    } else {
                        loading.visibility = View.GONE
                    }
                }
            })
        } else {
            Snackbar.make(findViewById(R.id.main), R.string.login_snackbar_input, Snackbar.LENGTH_LONG).show()
        }
    }

    private fun openMainActivity() {
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        finish()
    }
}