package com.example.android.network.repository.video

import com.example.android.network.YOUTUBE_API_KEY
import com.example.android.network.httprequest.HttpRequest
import com.example.android.network.httprequest.RequestCallback
import com.example.android.network.repository.Callback
import org.json.JSONObject

class VideoRepositoryImpl : VideoRepository {

    private lateinit var url: String

    override fun getVideoId(track: String, callback: Callback<String?>) {
        url =
            "https://www.googleapis.com/youtube/v3/search?part=snippet&q=$track&type=video&key=$YOUTUBE_API_KEY"

        HttpRequest.getInstance()?.load(url, object : RequestCallback {

            override fun onSuccess(result: String) {

                var videoId: String? = null

                val jsonObject = JSONObject(result)
                val videoArray = jsonObject.getJSONArray("items")

                for (i in 0 until videoArray.length()) {
                    val videoObject = videoArray.getJSONObject(i)
                    val videoIdObject = videoObject.getJSONObject("id")

                    videoId = videoIdObject.getString("videoId")

                    if (videoId != null) {
                        break
                    }
                }

                if (videoId != null) {
                    callback.onSuccess(videoId)
                } else {
                    callback.onError(NullPointerException("Video does not exist"))
                }
            }

            override fun onError(throwable: Throwable) {

            }
        })
    }
}