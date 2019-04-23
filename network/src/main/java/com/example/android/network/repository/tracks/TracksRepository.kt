package com.example.android.network.repository.tracks

import com.example.android.network.models.ArtistTrack
import com.example.android.network.models.Track
import com.example.android.network.repository.Callback

interface TracksRepository{

    fun getArtistTopTracks(artistName: String, callback: Callback<List<ArtistTrack>>)

    fun getTopTracks(callback: Callback<List<Track>>)
}