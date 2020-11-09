package com.android.loginapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_password.*

class PasswordActivity : AppCompatActivity() {
    private val password : Password = Password()
    private var pass1Cond : Boolean = false
    private var pass2Cond : Boolean = false
    private lateinit var databaseHandler : DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)

        this.databaseHandler = DatabaseHandler(this)

        password1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s : Editable?) {}
            override fun beforeTextChanged(s : CharSequence?, start : Int, count : Int, after : Int) {}

            override fun onTextChanged(s : CharSequence?, start : Int, before : Int, count : Int) {
                val pass1String : String = (password1.text).toString()
                val pass1Check : Boolean = pass1String.length >= password.minimum

                if (pass1Check && pass1String.length <= password.minimum) {
                    pass1Error.visibility = View.GONE
                    pass1Cond = true

                    password1.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorLight))
                    button()
                } else {
                    var feedback : String = "Password minimum ${password.minimum} karakter"

                    if (pass1Check) {
                        feedback = "Password maksimum ${password.maximum} karakter"
                    }

                    pass1Error.text = SpannableStringBuilder(feedback)
                    pass1Error.visibility = View.VISIBLE
                    pass1Cond = false

                    password1.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorDanger))
                }
            }
        })

        password2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s : Editable?) {}
            override fun beforeTextChanged(s : CharSequence?, start : Int, count : Int, after : Int) {}

            override fun onTextChanged(s : CharSequence?, start : Int, before : Int, count : Int) {
                if (((password1.text).toString()).equals((password2.text).toString())) {
                    pass2Error.visibility = View.GONE
                    pass2Cond = true

                    password2.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorLight))
                    button()
                } else {
                    pass2Error.visibility = View.VISIBLE
                    pass2Cond = false
                    pass2Error.text = SpannableStringBuilder("Kata Sandi harus sama dengan Konfirmasi Kata Sandi")

                    password2.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorDanger))
                }
            }
        })
    }

    fun ubah(view : View) {
        val pass1String : String = (password1.text).toString()

        if (pass1Cond && pass2Cond) {
            if (databaseHandler.update(User(SessionManager(this).sharedPreferences.getString("Email", "").toString(), password = pass1String))) {
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            } else {
                Snackbar.make(view, "Ubah Kata Sandi tidak berhasil", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    fun button() {
        if (pass1Cond && pass2Cond) {
            ubah.isEnabled = true
        } else {
            ubah.isEnabled = false
        }
    }
}
