package com.android.loginapp

import android.app.ListActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var sessionManager : SessionManager

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        this.sessionManager = SessionManager(this)

        if (this.sessionManager.sharedPreferences.getBoolean("LoggedIn", false)) {
            setContentView(R.layout.activity_main)

            var dRead = DatabaseHandler(this).read(User(email = this.sessionManager.sharedPreferences.getString("Email", "").toString()))
            name.text = dRead.get(0).name
        } else {
            login()
        }
    }

    fun logout(view : View) {
        this.sessionManager.clear()
        this.login()
    }

    fun login() {
        startActivity(Intent(applicationContext, LoginActivity::class.java))
        finish()
    }

    fun list(view : View) {
        onPause()
        startActivity(Intent(applicationContext, ListActivity::class.java))
    }

    fun password(view : View) {
        onPause()
        startActivity(Intent(applicationContext, PasswordActivity::class.java))
    }
}
