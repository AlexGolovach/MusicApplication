package com.example.android.network.repository

interface Callback<T> {

    fun onSuccess(result: T)

    fun onError(throwable: Throwable)
}