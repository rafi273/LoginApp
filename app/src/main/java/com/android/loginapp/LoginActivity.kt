package com.android.loginapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun login(view : View) {
        val email : String = (email.text).toString()
        val password : String = (password.text).toString()

        if (email.matches(Regex("[a-zA-Z0-9]+@[a-z]+\\.+[a-z]+")) && password != "") {
            val databaseHandler : DatabaseHandler = DatabaseHandler(this)
            val dRead = databaseHandler.read(User(email = email, password = password))

            if (dRead.size > 0) {
                SessionManager(this).create(dRead.get(0).email)
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            } else {
                Snackbar.make(view, "Email atau Password salah", Snackbar.LENGTH_SHORT).show()
            }
        } else {
            Snackbar.make(view, "Email dan Password harus diisi", Snackbar.LENGTH_SHORT).show()
        }
    }

    fun register(view : View) {
        onPause()
        startActivity(Intent(applicationContext, RegisterActivity::class.java))
    }
}
