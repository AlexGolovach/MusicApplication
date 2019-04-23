package com.example.android.database.repository.user

import com.example.android.database.Callback
import com.example.android.database.model.User

interface UserRepository {

    fun signUp(userName: String, userEmail: String, userPassword: String, callback: Callback<User>)

    fun login(userEmail: String, userPassword: String, callback: Callback<User>)

    fun firstEntry(callback: Callback<User>)

    fun logout(callback: Callback<User>)
}