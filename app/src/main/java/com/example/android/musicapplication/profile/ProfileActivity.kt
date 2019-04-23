package com.example.android.musicapplication.profile

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.android.musicapplication.R
import com.example.android.musicapplication.utils.getAccount
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_profile)

        initToolbar()
        getAccountInformation()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setTitleTextColor(resources.getColor(R.color.white))
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun getAccountInformation() {
        username.text = getAccount()?.username
        email.text = getAccount()?.email
        password.text = getAccount()?.password
    }
}