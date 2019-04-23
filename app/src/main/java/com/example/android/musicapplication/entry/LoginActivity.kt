package com.example.android.musicapplication.entry

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import com.example.android.database.Callback
import com.example.android.database.Injector
import com.example.android.database.model.User
import com.example.android.musicapplication.R
import com.example.android.musicapplication.activities.MainActivity
import com.example.android.musicapplication.utils.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import java.lang.NullPointerException

class LoginActivity : AppCompatActivity(), Callback<User> {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        initListeners()
    }

    private fun initListeners() {
        edit_email.addTextChangedListener(object : TextWatcherAdapter() {
            override fun afterTextChanged(s: Editable?) {
                val userEmail = edit_email.text.toString()

                edit_password.addTextChangedListener(object : TextWatcherAdapter() {
                    override fun afterTextChanged(s: Editable?) {
                        val userPassword = edit_password.text.toString()

                        updateButtonState(userEmail, userPassword)
                    }
                })
            }
        })

        sign_up.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun updateButtonState(userEmail: String, userPassword: String) {
        btn_login.isEnabled = true

        btn_login.setOnClickListener {

            val userRepository = Injector.getUserRepositoryImpl()
            userRepository.login(userEmail, userPassword, this)

        }
    }

    override fun onSuccess(result: User) {
        val gson = Gson()
        val user = gson.toJson(result)

        sharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString(APP_PREFERENCES,user)
        editor.apply()

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onError(throwable: Throwable) {
        if (throwable is NullPointerException) {

            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.error))
                .setMessage(getString(R.string.problem_with_entry))
                .setIcon(R.drawable.splash_screen_image)
                .setCancelable(false)
                .setNegativeButton(getString(R.string.ok)) { dialog, _ ->
                    dialog.cancel()
                }
                .create()
                .show()
        }
    }

}