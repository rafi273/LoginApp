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
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {
    private val CHAR_LIST : String = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"
    private val num : IntArray = intArrayOf(8, 10, 12)
    private val RANDOM_STRING_LENGTH : Int = num[Random().nextInt(2)]
    private var emailCond : Boolean = false
    private var nameCond : Boolean = false
    private var passCond : Boolean = false
    private var nameArr = arrayOfNulls<String>(3)
    private lateinit var databaseHandler : DatabaseHandler

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        this.databaseHandler = DatabaseHandler(this)

        email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s : Editable?) {}
            override fun beforeTextChanged(s : CharSequence?, start : Int, count : Int, after : Int) {}

            override fun onTextChanged(s: CharSequence?, start : Int, before: Int, count: Int) {
                val emailString : String = (email.text).toString()
                val emailCheck : Boolean = emailString.matches(Regex("[a-zA-Z0-9]+@[a-z]+\\.+[a-z]+"))

                if (emailCheck && (databaseHandler.read(User(email = emailString))).size == 0) {
                    emailHint.visibility = View.GONE
                    emailError.visibility = View.GONE
                    emailCond = true

                    email.setBackgroundColor(ContextCompat.getColor(applicationContext,
                        R.color.colorLight
                    ))
                    button()
                } else {
                    if (!emailCheck) {
                        emailError.text = SpannableStringBuilder("Formal email salah")
                        emailHint.visibility = View.GONE
                    } else {
                        var name : String = emailString.split("@")[0]
                        var generate : String = generateRandomString()
                        emailError.text = SpannableStringBuilder("Email sudah digunakan")
                        emailHint1.text = "${generateRandomString()}$name"
                        emailHint2.text = "$name${generateRandomString()}"
                        emailHint3.text = "${generate.subSequence(0, (generate.length / 2))}$name${generate.subSequence((generate.length / 2), (generate.length))}"
                        emailHint.visibility = View.VISIBLE
                    }

                    emailError.visibility = View.VISIBLE
                    emailCond = false

                    email.setBackgroundColor(ContextCompat.getColor(applicationContext,
                        R.color.colorDanger
                    ))
                }
            }
        })

        name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s : Editable?) {}
            override fun beforeTextChanged(s : CharSequence?, start : Int, count : Int, after : Int) {}

            override fun onTextChanged(s : CharSequence?, start : Int, before : Int, count : Int) {
                if ((name.text).toString() != "") {
                    nameError.visibility = View.GONE
                    nameCond = true

                    name.setBackgroundColor(ContextCompat.getColor(applicationContext,
                        R.color.colorLight
                    ))
                    button()
                } else {
                    nameError.visibility = View.VISIBLE
                    nameCond = false

                    name.setBackgroundColor(ContextCompat.getColor(applicationContext,
                        R.color.colorDanger
                    ))
                }
            }
        })

        password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s : Editable?) {}
            override fun beforeTextChanged(s : CharSequence?, start : Int, count : Int, after : Int) {}

            override fun onTextChanged(s : CharSequence?, start : Int, before : Int, count : Int) {
                val passwordCls : Password = Password()
                val min : Boolean = (password.text).length >= passwordCls.minimum

                if (min && (password.text).length <= passwordCls.maximum) {
                    passError.visibility = View.GONE
                    passCond = true

                    password.setBackgroundColor(ContextCompat.getColor(applicationContext,
                        R.color.colorLight
                    ))
                    button()
                } else {
                    var feedback : String = "Password minimum ${passwordCls.minimum} karakter"

                    if (min) {
                        feedback = "Password maksimum ${passwordCls.maximum} karakter"
                    }

                    passError.text = SpannableStringBuilder(feedback)
                    passError.visibility = View.VISIBLE
                    passCond = false

                    password.setBackgroundColor(ContextCompat.getColor(applicationContext,
                        R.color.colorDanger
                    ))
                }
            }
        })
    }

    fun register(view : View) {
        val emailString : String = (email.text).toString()
        val nameString : String = (name.text).toString()
        val passwordString : String = (password.text).toString()

        if (emailCond && nameCond && passCond) {
            if (databaseHandler.insert(User(emailString, nameString, passwordString))) {
                SessionManager(this).create(emailString)
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            } else {
                Snackbar.make(view, "Pendaftaran tidak berhasil", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    fun generate(view : View) {
        password.text = SpannableStringBuilder(generateRandomString())
    }

    fun login(view : View) {
        onPause()
        startActivity(Intent(applicationContext, LoginActivity::class.java))
    }

    fun button() {
        if (emailCond && nameCond && passCond) {
            daftar.isEnabled = true
        } else {
            daftar.isEnabled = false
        }
    }

    private fun generateRandomString() : String {
        val randStr = StringBuffer()

        for (i in 0 until RANDOM_STRING_LENGTH) {
            randStr.append(CHAR_LIST.get(getRandomNumber()))
        }

        return randStr.toString()
    }

    private fun getRandomNumber() : Int {
        var random : Int = Random().nextInt(CHAR_LIST.length)

        return if ((random - 1) == -1) {
            random
        } else {
            random - 1
        }
    }
}
