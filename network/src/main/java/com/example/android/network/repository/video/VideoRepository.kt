package com.example.android.network.repository.video

import com.example.android.network.repository.Callback

interface VideoRepository {

    fun getVideoId(track: String, callback: Callback<String?>)
}