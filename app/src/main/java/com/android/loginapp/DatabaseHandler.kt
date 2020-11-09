package com.android.loginapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues

class DatabaseHandler(context : Context) : SQLiteOpenHelper(context, DATABASE_NAME,null, DATABASE_VERSION) {
    companion object {
        private val DATABASE_VERSION : Int = 1
        private val DATABASE_NAME : String = "tbo"
        private val TABLE_CONTACTS : String = "account"
        private val KEY_EMAIL : String = "email"
        private val KEY_NAME : String = "name"
        private val KEY_PASSWORD : String = "password"
    }

    override fun onCreate(db : SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE $TABLE_CONTACTS ($KEY_EMAIL VARCHAR(30) PRIMARY KEY, $KEY_NAME VARCHAR(30) NOT NULL, $KEY_PASSWORD VARCHAR(30) NOT NULL)")
    }

    override fun onUpgrade(db : SQLiteDatabase?, oldVersion : Int, newVersion : Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTS")
        onCreate(db)
    }

    fun insert(user : User) : Boolean {
        val contentValues = ContentValues()

        contentValues.put(KEY_EMAIL, user.email)
        contentValues.put(KEY_NAME, user.name)
        contentValues.put(KEY_PASSWORD, user.password)

        val result : Long = this.writableDatabase.insert(TABLE_CONTACTS, null, contentValues)

        return if (result == (-1).toLong()) {
            false
        } else {
            true
        }
    }

    fun read(user : User) : ArrayList<User> {
        var query : String = "SELECT * FROM $TABLE_CONTACTS"

        if (user.email != "") {
            query += " WHERE email = '${user.email}'"

            if (user.password != "") {
                query += " AND password = '${user.password}'"
            }
        }

        val userList : ArrayList<User> = ArrayList<User>()
        var result = this.readableDatabase.rawQuery(query, null)

        if (result.moveToFirst()) {
            do {
                var user2 = User()
                user2.email = result.getString(result.getColumnIndex("email"))
                user2.name = result.getString(result.getColumnIndex("name"))
                user2.password = result.getString(result.getColumnIndex("password"))

                userList .add(user2)
            } while (result.moveToNext())
        }

        return userList
    }

    fun update(user : User) : Boolean {
        val contentValues = ContentValues()

        contentValues.put(KEY_PASSWORD, user.password)
        this.writableDatabase.update(TABLE_CONTACTS, contentValues, "$KEY_EMAIL = ?", arrayOf(user.email))
        return true
    }
}