package com.android.loginapp

import android.content.Context
import android.content.SharedPreferences

class SessionManager {
    lateinit var sharedPreferences : SharedPreferences

    constructor(context : Context) {
        this.sharedPreferences = context.getSharedPreferences("session", 0)
    }

    fun create(email : String, loggedin : Boolean = true) {
        val editor : SharedPreferences.Editor = this.sharedPreferences.edit()

        editor.putBoolean("LoggedIn", loggedin)
        editor.putString("Email", email)
        editor.apply()
    }

    fun clear() {
        this.create("", false)
    }
}