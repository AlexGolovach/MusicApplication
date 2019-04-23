package com.example.android.database

import android.content.Context
import android.preference.PreferenceManager
import com.example.android.database.repository.playlist.PlaylistRepository
import com.example.android.database.repository.playlist.PlaylistRepositoryImpl
import com.example.android.database.repository.user.UserRepository
import com.example.android.database.repository.user.UserRepositoryImpl

class Injector private constructor(context: Context) {

    private val userRepositoryImpl: UserRepositoryImpl =
        UserRepositoryImpl(
            PreferenceManager.getDefaultSharedPreferences(
                context
            )
        )
    private val playlistRepositoryImpl: PlaylistRepositoryImpl = PlaylistRepositoryImpl()

    companion object {

        @Volatile
        private var instance: Injector? = null

        private fun getInstance(): Injector? {
            if (instance == null){
                synchronized(Injector::class.java){
                    if (instance == null){
                        instance = Injector(App.get()!!)
                    }
                }
            }

            return instance
        }

        fun getUserRepositoryImpl(): UserRepository {
            return getInstance()!!.userRepositoryImpl
        }

        fun getPlaylistRepositoryImpl(): PlaylistRepository{
            return getInstance()!!.playlistRepositoryImpl
        }
    }
}