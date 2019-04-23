package com.example.android.database.repository.playlist

import com.example.android.database.Callback
import com.example.android.database.model.Playlist

interface PlaylistRepository {

    fun addTrackInPlaylist(userId: String, track: String, rating: Long)

    fun downloadPlaylist(userdId: String, callback: Callback<ArrayList<Playlist>>)

    fun deleteTrack(track: Playlist)

    fun updateRating(trackId: String, rating: Long)
}