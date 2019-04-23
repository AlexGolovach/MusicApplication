package com.example.android.musicapplication.entry

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import com.example.android.database.Callback
import com.example.android.database.Injector
import com.example.android.database.model.User
import com.example.android.musicapplication.R
import com.example.android.musicapplication.utils.TextWatcherAdapter
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity(), Callback<User> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_sign_up)

        initListeners()
    }

    private fun initListeners() {
        edit_username.addTextChangedListener(object : TextWatcherAdapter() {
            override fun afterTextChanged(s: Editable?) {
                val userName = edit_username.text.toString()

                edit_email.addTextChangedListener(object : TextWatcherAdapter() {
                    override fun afterTextChanged(s: Editable?) {
                        val userEmail = edit_email.text.toString()

                        edit_password.addTextChangedListener(object : TextWatcherAdapter() {
                            override fun afterTextChanged(s: Editable?) {
                                val userPassword = edit_password.text.toString()

                                updateButtonState(userName, userEmail, userPassword)
                            }
                        })
                    }
                })
            }
        })
    }

    private fun updateButtonState(
        userName: String,
        userEmail: String,
        userPassword: String
    ) {
        btn_sign_up.isEnabled = true

        btn_sign_up.setOnClickListener {

            val userRepository = Injector.getUserRepositoryImpl()
            userRepository.signUp(userName, userEmail, userPassword, this)
        }
    }

    override fun onSuccess(result: User) {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun onError(throwable: Throwable) {
        if (throwable is NullPointerException) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.error)
                .setMessage(R.string.problem_with_entry)
                .setIcon(R.drawable.splash_screen_image)
                .setCancelable(false)
                .setNegativeButton(R.string.ok) { dialog, _ ->
                    dialog.cancel()
                }
            val alertDialog = builder.create()
            alertDialog.show()
        }
    }
}