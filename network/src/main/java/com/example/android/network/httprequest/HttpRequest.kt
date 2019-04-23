package com.example.android.network.httprequest

import android.accounts.NetworkErrorException
import android.content.Context
import android.net.ConnectivityManager
import android.os.AsyncTask
import com.example.android.database.App
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URL

class HttpRequest private constructor() {

    companion object {

        @Volatile
        private var instance: HttpRequest? = null

        fun getInstance(): HttpRequest? {
            if (instance == null) {
                synchronized(HttpRequest::class.java) {
                    if (instance == null) {
                        instance = HttpRequest()
                    }
                }
            }

            return instance
        }
    }

    fun load(url: String, callback: RequestCallback) {
        val connectivityManager =
            App.get()?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo

        if (networkInfo != null && networkInfo.isConnected) {
            LoaderTask(callback).execute(url)
        } else {
            callback.onError(NetworkErrorException("No internet connection"))
        }
    }

    private class LoaderTask(var callback: RequestCallback) :
        AsyncTask<String, Void, String>() {

        lateinit var url: String

        override fun doInBackground(vararg urls: String): String? {
            url = urls[0]

            val url = URL(url)

            val client = OkHttpClient()
            val request = Request.Builder()
                .get()
                .url(url)
                .build()

            return client.newCall(request).execute().body().string()
        }

        override fun onPostExecute(string: String) {
            callback.onSuccess(string)
        }
    }
}