package com.example.android.network.httprequest

interface RequestCallback {

    fun onSuccess(result: String)

    fun onError(throwable: Throwable)
}