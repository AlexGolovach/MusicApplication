package com.example.android.database

import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        sInstance = this
    }

    companion object {

        private var sInstance: App? = null

        fun get(): App? {
            return sInstance
        }
    }
}