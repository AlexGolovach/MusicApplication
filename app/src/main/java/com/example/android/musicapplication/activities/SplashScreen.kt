package com.example.android.musicapplication.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.example.android.database.Callback
import com.example.android.database.Injector
import com.example.android.database.model.User
import com.example.android.musicapplication.entry.LoginActivity
import java.lang.NullPointerException

class SplashScreen : AppCompatActivity(), Callback<User> {

    private val SPLASH_DISPLAY_LENGTH = 1000

    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handler = Handler()
        handler.postDelayed({
            val userRepository = Injector.getUserRepositoryImpl()
            userRepository.firstEntry(this)

        }, SPLASH_DISPLAY_LENGTH.toLong())
    }

    override fun onDestroy() {
        super.onDestroy()

        handler.removeCallbacksAndMessages(null)
    }

    override fun onSuccess(result: User) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onError(throwable: Throwable) {
        if (throwable is NullPointerException) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}