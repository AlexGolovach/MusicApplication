package com.example.android.database.repository.user

import android.content.SharedPreferences
import android.text.TextUtils
import android.util.Log
import com.example.android.database.Callback
import com.example.android.database.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson

class UserRepositoryImpl(private val sharedPreferences: SharedPreferences) :
    UserRepository {

    private var currentUser: User
        get() {
            val gson = Gson()
            val userJson = sharedPreferences.getString(USER_KEY, null)

            return gson.fromJson(userJson, User::class.java)
        }
        set(user) {
            val gson = Gson()
            val account = gson.toJson(user)
            val editor = sharedPreferences.edit()
            editor.putString(USER_KEY, account)
            editor.apply()
        }

    override fun signUp(
        userName: String,
        userEmail: String,
        userPassword: String,
        callback: Callback<User>
    ) {
        val databaseUsers = FirebaseDatabase.getInstance().getReference("users")


        val userNameQuery = databaseUsers
            .orderByChild("users")
            .equalTo(userName)

        userNameQuery.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (!dataSnapshot.exists()) {
                    val userEmailQuery = databaseUsers
                        .orderByChild("users")
                        .equalTo(userEmail)

                    userEmailQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (!dataSnapshot.exists()) {

                                val userId = databaseUsers.push().key
                                val user = User(userId!!, userName, userEmail, userPassword)

                                databaseUsers.child(userId).setValue(user)

                                callback.onSuccess(user)
                            } else {
                                callback.onError(NullPointerException("User exist!"))
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            callback.onError(databaseError.toException())
                        }
                    })
                } else {
                    callback.onError(NullPointerException("User exist!"))
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback.onError(databaseError.toException())
            }

        })
    }

    override fun login(userEmail: String, userPassword: String, callback: Callback<User>) {
        val userEmailQuery = FirebaseDatabase.getInstance().getReference("users")
            .orderByChild("email")
            .equalTo(userEmail)

        userEmailQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {

                    val userPasswordQuery = FirebaseDatabase.getInstance().getReference("users")
                        .orderByChild("password")
                        .equalTo(userPassword)

                    userPasswordQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {

                                for (childSnapshot in dataSnapshot.children) {
                                    val userId = childSnapshot.key
                                    val user = childSnapshot.getValue(User::class.java)

                                    val account =
                                        User(userId!!, user!!.username, userEmail, userPassword)
                                    currentUser = account
                                    callback.onSuccess(account)
                                }

                            } else {
                                callback.onError(NullPointerException("User not found."))
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            callback.onError(databaseError.toException())
                        }
                    })

                } else {
                    callback.onError(NullPointerException("User not found."))
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback.onError(databaseError.toException())
            }
        })
    }

    override fun firstEntry(callback: Callback<User>) {
        if (sharedPreferences.getString("KEY", null) != null) {
            callback.onSuccess(currentUser)
        } else {
            callback.onError(NullPointerException("User not found."))
        }
    }

    override fun logout(callback: Callback<User>) {
        val editor = sharedPreferences.edit()

        callback.onSuccess(currentUser)

        editor.remove("KEY")
        editor.clear()
        editor.apply()
    }

    companion object {
        private const val USER_KEY = "KEY"
    }

}