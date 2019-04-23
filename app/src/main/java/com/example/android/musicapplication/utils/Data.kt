package com.example.android.musicapplication.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AlertDialog
import com.example.android.database.App
import com.example.android.database.model.User
import com.example.android.musicapplication.R
import com.google.gson.Gson

const val APP_PREFERENCES = "ACCOUNT"

private val sharedPreferences =
    App.get()?.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

enum class SearchResultType {
    NULL,
    NOT_NULL
}

fun getAccount(): User?{
    val gson = Gson()

    val json = sharedPreferences?.getString(APP_PREFERENCES,"")

    return gson.fromJson(json,User::class.java)
}

fun removeAccountInformation() {
    val editor: SharedPreferences.Editor = sharedPreferences!!.edit()

    editor.remove(APP_PREFERENCES)
    editor.clear()
    editor.apply()
}

fun showMyDialog(activity: Activity) {
    val alertDialog = AlertDialog.Builder(activity)

    alertDialog
        .setTitle("Error")
        .setMessage("No internet connection")
        .setPositiveButton("Ok") { _, _ ->
        }
        .create()
        .show()
}

fun getRating(playcount: Long): String {
    val rating = listOf(App.get()?.getString(R.string.playcount), playcount)

    return rating.joinToString(separator = ": ")
}
