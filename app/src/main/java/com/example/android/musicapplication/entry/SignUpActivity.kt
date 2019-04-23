package com.example.android.musicapplication.entry

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.widget.Toast
import com.example.android.database.Callback
import com.example.android.database.Injector
import com.example.android.database.model.User
import com.example.android.musicapplication.R
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity(), Callback<User> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_sign_up)

        initListeners()
    }

    private fun initListeners() {
        btn_sign_up.setOnClickListener {
            if (!TextUtils.isEmpty(edit_username.text.toString()) && !TextUtils.isEmpty(edit_email.text.toString()) && !TextUtils.isEmpty(
                    edit_password.text.toString()
                )
            ) {
                val userRepository = Injector.getUserRepositoryImpl()
                userRepository.signUp(
                    edit_username.text.toString(),
                    edit_email.text.toString(),
                    edit_password.text.toString(),
                    this
                )
            } else {
                Toast.makeText(this, "Fields must not be empty", Toast.LENGTH_SHORT).show()
            }

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