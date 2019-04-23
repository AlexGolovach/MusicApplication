package com.example.android.imageloader

import android.graphics.Bitmap

interface Callback {
    fun onSuccess(url: String, bitmap: Bitmap)

    fun onError(url: String, throwable: Throwable)
}