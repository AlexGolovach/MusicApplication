package com.example.android.network

import com.example.android.network.repository.artists.ArtistRepository
import com.example.android.network.repository.artists.ArtistRepositoryImpl
import com.example.android.network.repository.tracks.TracksRepository
import com.example.android.network.repository.tracks.TracksRepositoryImpl
import com.example.android.network.repository.video.VideoRepository
import com.example.android.network.repository.video.VideoRepositoryImpl

class Injector private constructor() {

    private val artistRepositoryImpl: ArtistRepositoryImpl =
        ArtistRepositoryImpl()
    private val tracksRepositoryImpl: TracksRepositoryImpl = TracksRepositoryImpl()
    private val videoRepositoryImpl: VideoRepositoryImpl = VideoRepositoryImpl()

    companion object {

        @Volatile
        private var instance: Injector? = null

        private fun getInstance(): Injector? {
            if (instance == null) {
                synchronized(Injector::class.java) {
                    if (instance == null) {
                        instance = Injector()
                    }
                }
            }

            return instance
        }

        fun getArtistsRepositoryImpl(): ArtistRepository {
            return getInstance()!!.artistRepositoryImpl
        }

        fun getTracksRepositoryImpl(): TracksRepository {
            return getInstance()!!.tracksRepositoryImpl
        }

        fun getVideoRepositoryImpl(): VideoRepository {
            return getInstance()!!.videoRepositoryImpl
        }
    }
}