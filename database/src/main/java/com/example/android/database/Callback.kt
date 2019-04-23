package com.example.android.database

interface Callback<T>{

    fun onSuccess(result: T)

    fun onError(throwable: Throwable)
}